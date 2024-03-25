/******************************************************************************
 * This file is auto-generated by Vaadin.
 * It configures React Router automatically by adding server-side (Flow) routes,
 * which is enough for Vaadin Flow applications.
 * Once any `.tsx` or `.jsx` React routes are added into
 * `src/main/frontend/views/` directory, this route configuration is
 * re-generated automatically by Vaadin.
 ******************************************************************************/
import { createBrowserRouter, RouteObject } from 'react-router-dom';
import { serverSideRoutes } from 'Frontend/generated/flow/Flow';

export const routes = [
    ...serverSideRoutes
] as RouteObject[];

export default createBrowserRouter(routes);
