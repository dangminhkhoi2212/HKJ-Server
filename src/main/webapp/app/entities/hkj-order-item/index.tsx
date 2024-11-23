import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjOrderItem from './hkj-order-item';
import HkjOrderItemDetail from './hkj-order-item-detail';
import HkjOrderItemUpdate from './hkj-order-item-update';
import HkjOrderItemDeleteDialog from './hkj-order-item-delete-dialog';

const HkjOrderItemRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjOrderItem />} />
    <Route path="new" element={<HkjOrderItemUpdate />} />
    <Route path=":id">
      <Route index element={<HkjOrderItemDetail />} />
      <Route path="edit" element={<HkjOrderItemUpdate />} />
      <Route path="delete" element={<HkjOrderItemDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjOrderItemRoutes;
