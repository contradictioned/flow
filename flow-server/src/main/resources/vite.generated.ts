/**
 * NOTICE: this is an auto-generated file
 *
 * This file has been generated by the `flow:prepare-frontend` maven goal.
 * This file will be overwritten on every run. Any custom changes should be made to vite.config.ts
 */
import path from 'path';
import { readFileSync, existsSync, writeFileSync } from 'fs';
import * as net from 'net';

import { processThemeResources } from '#buildFolder#/plugins/application-theme-plugin/theme-handle';
import { rewriteCssUrls } from '#buildFolder#/plugins/theme-loader/theme-loader-utils';
import settings from '#settingsImport#';
import { defineConfig, mergeConfig, PluginOption, ResolvedConfig, UserConfigFn, OutputOptions, AssetInfo, ChunkInfo } from 'vite';
import { injectManifest } from 'workbox-build';

import * as rollup from 'rollup';
import brotli from 'rollup-plugin-brotli';
import replace from '@rollup/plugin-replace';
import checker from 'vite-plugin-checker';
import postcssLit from '#buildFolder/plugins/rollup-plugin-postcss-lit-custom';

const appShellUrl = '.';

const frontendFolder = path.resolve(__dirname, settings.frontendFolder);
const themeFolder = path.resolve(frontendFolder, settings.themeFolder);
const frontendBundleFolder = path.resolve(__dirname, settings.frontendBundleOutput);
const addonFrontendFolder = path.resolve(__dirname, settings.addonFrontendFolder);
const themeResourceFolder = path.resolve(__dirname, settings.themeResourceFolder);
const statsFile = path.resolve(frontendBundleFolder, '..', 'config', 'stats.json');

const projectStaticAssetsFolders = [
  path.resolve(__dirname, 'src', 'main', 'resources', 'META-INF', 'resources'),
  path.resolve(__dirname, 'src', 'main', 'resources', 'static'),
  frontendFolder
];

// Folders in the project which can contain application themes
const themeProjectFolders = projectStaticAssetsFolders.map((folder) => path.resolve(folder, settings.themeFolder));

const themeOptions = {
  devMode: false,
  // The following matches folder 'target/flow-frontend/themes/'
  // (not 'frontend/themes') for theme in JAR that is copied there
  themeResourceFolder: path.resolve(themeResourceFolder, settings.themeFolder),
  themeProjectFolders: themeProjectFolders,
  projectStaticAssetsOutputFolder: path.resolve(__dirname, settings.staticOutput),
  frontendGeneratedFolder: path.resolve(frontendFolder, settings.generatedFolder)
};

const hasExportedWebComponents = existsSync(path.resolve(frontendFolder, 'web-component.html'));

// Block debug and trace logs.
console.trace = () => {};
console.debug = () => {};

function buildSWPlugin(opts): PluginOption {
  let config: ResolvedConfig;
  const devMode = opts.devMode;

  const swObj = {}

  async function build(action: 'generate' | 'write') {
    const includedPluginNames = [
      'alias',
      'vite:resolve',
      'vite:esbuild',
      'rollup-plugin-dynamic-import-variables',
      'vite:esbuild-transpile',
      'vite:terser',
    ]
    const plugins: rollup.Plugin[] = config.plugins.filter((p) => {
      return includedPluginNames.includes(p.name)
    });
    plugins.push(
      replace({
        values: {
          'process.env.NODE_ENV': JSON.stringify(config.mode),
          ...config.define,
        },
        preventAssignment: true
      })
    );
    const bundle = await rollup.rollup({
      input: path.resolve(settings.clientServiceWorkerSource),
      plugins
    });

    try {
      return await bundle[action]({
        file: path.resolve(frontendBundleFolder, 'sw.js'),
        format: 'es',
        exports: 'none',
        sourcemap: config.command === 'serve' || config.build.sourcemap,
        inlineDynamicImports: true,
      });
    } finally {
      await bundle.close();
    }
  }

  return {
    name: 'vaadin:build-sw',
    enforce: 'post',
    async configResolved(resolvedConfig) {
      config = resolvedConfig;
    },
    async buildStart() {
      if (devMode) {
        const { output } = await build('generate');
        swObj.code = output[0].code;
        swObj.map =  output[0].map;
      } else {
        await build('write');
      }
    },
    async load(id) {
      if (id.endsWith('sw.js')) {
        return '';
      }
    },
    async transform(_code, id) {
      if (id.endsWith('sw.js')) {
        return swObj;
      }
    },
  }
}

function injectManifestToSWPlugin(): PluginOption {
  const rewriteManifestIndexHtmlUrl = (manifest) => {
    const indexEntry = manifest.find((entry) => entry.url === 'index.html');
    if (indexEntry) {
      indexEntry.url = appShellUrl;
    }

    return { manifest, warnings: [] };
  };

  return {
    name: 'vaadin:inject-manifest-to-sw',
    enforce: 'post',
    apply: 'build',
    async closeBundle() {
      await injectManifest({
        swSrc: path.resolve(frontendBundleFolder, 'sw.js'),
        swDest: path.resolve(frontendBundleFolder, 'sw.js'),
        globDirectory: frontendBundleFolder,
        globPatterns: ['**/*'],
        globIgnores: ['**/*.br'],
        injectionPoint: 'self.__WB_MANIFEST',
        manifestTransforms: [rewriteManifestIndexHtmlUrl],
        maximumFileSizeToCacheInBytes: 100 * 1024 * 1024 // 100mb,
      });
    }
  };
}

function statsExtracterPlugin(): PluginOption {
  return {
    name: 'vaadin:stats',
    enforce: 'post',
    async writeBundle(options: OutputOptions, bundle: { [fileName: string]: AssetInfo | ChunkInfo }) {
      const modules = Object.values(bundle).flatMap((b) => (b.modules ? Object.keys(b.modules) : []));
      const nodeModulesFolders = modules.filter((id) => id.includes('node_modules'));
      const npmModules = nodeModulesFolders
        .map((id) => id.replace(/.*node_modules./, ''))
        .map((id) => {
          const parts = id.split('/');
          if (id.startsWith('@')) {
            return parts[0] + '/' + parts[1];
          } else {
            return parts[0];
          }
        })
        .sort()
        .filter((value, index, self) => self.indexOf(value) === index);

      writeFileSync(statsFile, JSON.stringify({ npmModules }, null, 1));
    }
  };
}
function vaadinBundlesPlugin(): PluginOption {
  type ExportInfo =
    | string
    | {
        namespace?: string;
        source: string;
      };

  type ExposeInfo = {
    exports: ExportInfo[];
  };

  type PackageInfo = {
    version: string;
    exposes: Record<string, ExposeInfo>;
  };

  type BundleJson = {
    packages: Record<string, PackageInfo>;
  };

  const disabledMessage = 'Vaadin component dependency bundles are disabled.';

  const modulesDirectory = path.resolve(__dirname, 'node_modules').replace(/\\/g, '/');

  let vaadinBundleJson: BundleJson;

  function parseModuleId(id: string): { packageName: string; modulePath: string } {
    const [scope, scopedPackageName] = id.split('/', 3);
    const packageName = scope.startsWith('@') ? `${scope}/${scopedPackageName}` : scope;
    const modulePath = `.${id.substring(packageName.length)}`;
    return {
      packageName,
      modulePath
    };
  }

  function getExports(id: string): string[] | undefined {
    const { packageName, modulePath } = parseModuleId(id);
    const packageInfo = vaadinBundleJson.packages[packageName];

    if (!packageInfo) return;

    const exposeInfo: ExposeInfo = packageInfo.exposes[modulePath];
    if (!exposeInfo) return;

    const exportsSet = new Set<string>();
    for (const e of exposeInfo.exports) {
      if (typeof e === 'string') {
        exportsSet.add(e);
      } else {
        const { namespace, source } = e;
        if (namespace) {
          exportsSet.add(namespace);
        } else {
          const sourceExports = getExports(source);
          if (sourceExports) {
            sourceExports.forEach((e) => exportsSet.add(e));
          }
        }
      }
    }
    return Array.from(exportsSet);
  }

  return {
    name: 'vaadin:bundles',
    enforce: 'pre',
    apply(config, { command }) {
      if (command !== 'serve') return false;

      try {
        const vaadinBundleJsonPath = require.resolve('@vaadin/bundles/vaadin-bundle.json');
        vaadinBundleJson = JSON.parse(readFileSync(vaadinBundleJsonPath, { encoding: 'utf8' }));
      } catch (e: unknown) {
        if (typeof e === 'object' && (e as { code: string }).code === 'MODULE_NOT_FOUND') {
          vaadinBundleJson = { packages: {} };
          console.info(`@vaadin/bundles npm package is not found, ${disabledMessage}`);
          return false;
        } else {
          throw e;
        }
      }

      const versionMismatches: Array<{ name: string; bundledVersion: string; installedVersion: string }> = [];
      for (const [name, packageInfo] of Object.entries(vaadinBundleJson.packages)) {
        let installedVersion: string | undefined = undefined;
        try {
          const { version: bundledVersion } = packageInfo;
          const installedPackageJsonFile = path.resolve(modulesDirectory, name, 'package.json');
          const packageJson = JSON.parse(readFileSync(installedPackageJsonFile, { encoding: 'utf8' }));
          installedVersion = packageJson.version;
          if (installedVersion && installedVersion !== bundledVersion) {
            versionMismatches.push({
              name,
              bundledVersion,
              installedVersion
            });
          }
        } catch (_) {
          // ignore package not found
        }
      }
      if (versionMismatches.length) {
        console.info(`@vaadin/bundles has version mismatches with installed packages, ${disabledMessage}`);
        console.info(`Packages with version mismatches: ${JSON.stringify(versionMismatches, undefined, 2)}`);
        vaadinBundleJson = { packages: {} };
        return false;
      }

      return true;
    },
    async config(config) {
      return mergeConfig(
        {
          optimizeDeps: {
            exclude: [
              // Vaadin bundle
              '@vaadin/bundles',
              ...Object.keys(vaadinBundleJson.packages)
            ]
          }
        },
        config
      );
    },
    load(rawId) {
      const [path, params] = rawId.split('?');
      if (!path.startsWith(modulesDirectory)) return;

      const id = path.substring(modulesDirectory.length + 1);
      const exports = getExports(id);
      if (exports === undefined) return;

      const cacheSuffix = params ? `?${params}` : '';
      const bundlePath = `@vaadin/bundles/vaadin.js${cacheSuffix}`;

      return `import { init as VaadinBundleInit, get as VaadinBundleGet } from '${bundlePath}';
await VaadinBundleInit('default');
const { ${exports.join(', ')} } = (await VaadinBundleGet('./node_modules/${id}'))();
export { ${exports.map((binding) => `${binding} as ${binding}`).join(', ')} };`;
    }
  };
}

function themePlugin(opts): PluginOption {
  const fullThemeOptions = {...themeOptions, devMode: opts.devMode };
  return {
    name: 'vaadin:theme',
    config() {
      processThemeResources(fullThemeOptions, console);
    },
    configureServer(server) {
      function handleThemeFileCreateDelete(themeFile, stats) {
        if (themeFile.startsWith(themeFolder)) {
          const changed = path.relative(themeFolder, themeFile)
          console.debug('Theme file ' + (!!stats ? 'created' : 'deleted'), changed);
          processThemeResources(fullThemeOptions, console);
        }
      }
      server.watcher.on('add', handleThemeFileCreateDelete);
      server.watcher.on('unlink', handleThemeFileCreateDelete);
    },
    handleHotUpdate(context) {
      const contextPath = path.resolve(context.file);
      const themePath = path.resolve(themeFolder);
      if (contextPath.startsWith(themePath)) {
        const changed = path.relative(themePath, contextPath);

        console.debug('Theme file changed', changed);

        if (changed.startsWith(settings.themeName)) {
          processThemeResources(fullThemeOptions, console);
        }
      }
    },
    async resolveId(id, importer) {
      // force theme generation if generated theme sources does not yet exist
      // this may happen for example during Java hot reload when updating
      // @Theme annotation value
      if (path.resolve(themeOptions.frontendGeneratedFolder, "theme.js") === importer &&
            !existsSync(path.resolve(themeOptions.frontendGeneratedFolder, id))) {
          console.debug('Generate theme file ' + id + ' not existing. Processing theme resource');
          processThemeResources(fullThemeOptions, console);
          return;
      }
      if (!id.startsWith(settings.themeFolder)) {
        return;
      }

      for (const location of [themeResourceFolder, frontendFolder]) {
        const result = await this.resolve(path.resolve(location, id));
        if (result) {
          return result;
        }
      }
    },
    async transform(raw, id, options) {
      // rewrite urls for the application theme css files
      const [bareId, query] = id.split('?');
      if (!bareId?.startsWith(themeFolder) || !bareId?.endsWith(".css")) {
        return;
      }
      const [themeName] = bareId.substring(themeFolder.length + 1).split('/');
      return rewriteCssUrls(raw, path.dirname(bareId), path.resolve(themeFolder, themeName), console, opts);
    }
  }
}

function runWatchDog(watchDogPort, watchDogHost) {
  const client = net.Socket();
  client.setEncoding('utf8');
  client.on('error', function (err) {
    console.log('Watchdog connection error. Terminating vite process...', err);
    client.destroy();
    process.exit(0);
  });
  client.on('close', function () {
    client.destroy();
    runWatchDog(watchDogPort, watchDogHost);
  });

  client.connect(watchDogPort, watchDogHost || 'localhost');
}

let spaMiddlewareForceRemoved = false;

const allowedFrontendFolders = [
  frontendFolder,
  addonFrontendFolder,
  path.resolve(addonFrontendFolder, '..', 'frontend'), // Contains only generated-flow-imports
  path.resolve(__dirname, 'node_modules')
];

function setHmrPortToServerPort(): PluginOption {
  return {
    name: 'set-hmr-port-to-server-port',
    configResolved(config) {
      if (config.server.strictPort && config.server.hmr !== false) {
        if (config.server.hmr === true) config.server.hmr = {};
        config.server.hmr = config.server.hmr || {};
        config.server.hmr.clientPort = config.server.port;
      }
    }
  };
}

export const vaadinConfig: UserConfigFn = (env) => {
  const devMode = env.mode === 'development';

  if (devMode && process.env.watchDogPort) {
    // Open a connection with the Java dev-mode handler in order to finish
    // vite when it exits or crashes.
    runWatchDog(process.env.watchDogPort, process.env.watchDogHost);
  }

  return {
    root: frontendFolder,
    base: '',
    resolve: {
      alias: {
        Frontend: frontendFolder
      },
      preserveSymlinks: true
    },
    define: {
      OFFLINE_PATH: settings.offlinePath,
      VITE_ENABLED: 'true'
    },
    server: {
      host: '127.0.0.1',
      strictPort: true,
      fs: {
        allow: allowedFrontendFolders
      }
    },
    build: {
      outDir: frontendBundleFolder,
      assetsDir: 'VAADIN/build',
      rollupOptions: {
        input: {
          indexhtml: path.resolve(frontendFolder, 'index.html'),

          ...hasExportedWebComponents
            ? { webcomponenthtml: path.resolve(frontendFolder, 'web-component.html') }
            : {}
        }
      }
    },
    optimizeDeps: {
      entries: [
        // Pre-scan entrypoints in Vite to avoid reloading on first open
        'generated/vaadin.ts'
      ],
      exclude: [
        '@vaadin/router',
        '@vaadin/vaadin-license-checker',
        '@vaadin/vaadin-usage-statistics',
        'workbox-core',
        'workbox-precaching',
        'workbox-routing',
        'workbox-strategies'
      ]
    },
    plugins: [
      !devMode && brotli(),
      devMode && vaadinBundlesPlugin(),
      devMode && setHmrPortToServerPort(),
      settings.offlineEnabled && buildSWPlugin({ devMode }),
      settings.offlineEnabled && injectManifestToSWPlugin(),
      !devMode && statsExtracterPlugin(),
      themePlugin({devMode}),
      postcssLit({
        include: ['**/*.css', '**/*.css\?*'],
        exclude: [
          `${themeFolder}/**/*.css`,
          `${themeFolder}/**/*.css\?*`,
          `${themeResourceFolder}/**/*.css`,
          `${themeResourceFolder}/**/*.css\?*`,
          '**/*\?html-proxy*'
        ]
      }),
      {
        name: 'vaadin:force-remove-spa-middleware',
        transformIndexHtml: {
          enforce: 'pre',
          transform(_html, { server }) {
            if (server && !spaMiddlewareForceRemoved) {
              server.middlewares.stack = server.middlewares.stack.filter((mw) => {
                const handleName = '' + mw.handle;
                return !handleName.includes('viteSpaFallbackMiddleware');
              });
              spaMiddlewareForceRemoved = true;
            }
          }
        }
      },
      hasExportedWebComponents && {
        name: 'vaadin:inject-entrypoints-to-web-component-html',
        transformIndexHtml: {
          enforce: 'pre',
          transform(_html, { path, server }) {
            if (path !== '/web-component.html') {
              return;
            }

            return [
              {
                tag: 'script',
                attrs: { type: 'module', src: `/generated/vaadin-web-component.ts` },
                injectTo: 'head'
              }
            ]
          }
        }
      },
      {
        name: 'vaadin:inject-entrypoints-to-index-html',
        transformIndexHtml: {
          enforce: 'pre',
          transform(_html, { path, server }) {
            if (path !== '/index.html') {
              return;
            }

            const scripts = [];

            if (devMode) {
              scripts.push({
                tag: 'script',
                attrs: { type: 'module', src: `/generated/vite-devmode.ts` },
                injectTo: 'head'
              });
            }
            scripts.push({
              tag: 'script',
              attrs: { type: 'module', src: '/generated/vaadin.ts' },
              injectTo: 'head'
            });
            return scripts;
          }
        }
      },
      checker({
        typescript: true
      })
    ]
  };
};

export const overrideVaadinConfig = (customConfig: UserConfigFn) => {
  return defineConfig((env) => mergeConfig(vaadinConfig(env), customConfig(env)));
};
