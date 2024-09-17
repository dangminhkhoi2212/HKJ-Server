import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjOrder from './hkj-order';
import HkjOrderDetail from './hkj-order-detail';
import HkjOrderUpdate from './hkj-order-update';
import HkjOrderDeleteDialog from './hkj-order-delete-dialog';

const HkjOrderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjOrder />} />
    <Route path="new" element={<HkjOrderUpdate />} />
    <Route path=":id">
      <Route index element={<HkjOrderDetail />} />
      <Route path="edit" element={<HkjOrderUpdate />} />
      <Route path="delete" element={<HkjOrderDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjOrderRoutes;
