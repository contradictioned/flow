/******************************************************************************
 * This file is auto-generated by Vaadin.
 * If you want to customize the entry point, you can copy this file or create
 * your own `index.tsx` in your frontend directory.
 * By default, the `index.tsx` file should be in `./frontend/` folder.
 *
 * NOTE:
 *     - You need to restart the dev-server after adding the new `index.tsx` file.
 *       After that, all modifications to `index.tsx` are recompiled automatically.
 *     - `index.js` is also supported if you don't want to use TypeScript.
 ******************************************************************************/

import { createElement } from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { router } from '%routesJsImportPath%';

function App() {
    return <RouterProvider router={router} />;
}

createRoot(document.getElementById('outlet')!).render(createElement(App));
