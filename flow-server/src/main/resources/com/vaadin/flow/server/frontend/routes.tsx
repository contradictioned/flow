/******************************************************************************
 * This file is auto-generated by Vaadin.
 * It configures React Router automatically by looking for React views files,
 * located in `src/main/frontend/views/` directory.
 * A manual configuration can be done as well, you have to:
 * - copy this file or create your own `routes.tsx` in your frontend directory,
 *   then modify this copied/created file. By default, the `routes.tsx` file
 *   should be in `src/main/frontend/` folder;
 * - use `RouterBuilder` API to configure routes for the application;
 * - restart the application, so that the imports get re-generated.
 *
 * `RouterBuilder` combines a File System-based route configuration or your
 * explicit routes configuration with the server-side routes.
 *
 * It has the following methods:
 * - `withFileRoutes` enables the File System-based routes autoconfiguration;
 * - `withReactRoutes` adds manual explicit route hierarchy. Allows also to add
 * an individual route, which then merged into File System-based routes,
 * e.g. Log In view;
 * - `withServerFallback` adds server-side routes automatically to either
 * autoconfigured routes, or manually configured routes;
 * - `protect` adds an authentication later to the routes.
 *
 * NOTE:
 * - You need to restart the dev-server after adding the new `routes.tsx` file.
 * After that, all modifications to `routes.tsx` are recompiled automatically.
 * - You may need to change a routes import in `index.tsx`, if `index.tsx`
 * exists in the frontend folder (not in generated folder) and you copied the file,
 * as the import isn't updated automatically by Vaadin in this case.
 ******************************************************************************/
import { RouterBuilder } from '@vaadin/hilla-file-router/runtime.js';
import { serverRoute } from 'Frontend/generated/flow/server-route';
import fileRoutes from 'Frontend/generated/file-routes';

const routerBuilder = new RouterBuilder()
    .withFileRoutes(fileRoutes) // (1)
    // To define routes manually, use the following code and remove (1):
    // .withReactRoutes([
    //     {
    //         element: <MainLayout />,
    //         handle: { title: 'Main' },
    //         children: [
    //             { path: '/hilla', element: <HillaView />, handle: { title: 'Hilla' } }
    //         ],
    //     },
    // ])
    .withServerFallback(serverRoute)
    // To add individual routes, use the following code:
    // .withReactRoutes([
    //     { path: '/login', element: <Login />, handle: { title: 'Login' } },
    // ])
    .protect();

export const routes = routerBuilder.routes;

export default routerBuilder.build();
