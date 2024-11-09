import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjCart from './hkj-cart';
import HkjCartDetail from './hkj-cart-detail';
import HkjCartUpdate from './hkj-cart-update';
import HkjCartDeleteDialog from './hkj-cart-delete-dialog';

const HkjCartRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjCart />} />
    <Route path="new" element={<HkjCartUpdate />} />
    <Route path=":id">
      <Route index element={<HkjCartDetail />} />
      <Route path="edit" element={<HkjCartUpdate />} />
      <Route path="delete" element={<HkjCartDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjCartRoutes;
