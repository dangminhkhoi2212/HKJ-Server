import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjPosition from './hkj-position';
import HkjPositionDetail from './hkj-position-detail';
import HkjPositionUpdate from './hkj-position-update';
import HkjPositionDeleteDialog from './hkj-position-delete-dialog';

const HkjPositionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjPosition />} />
    <Route path="new" element={<HkjPositionUpdate />} />
    <Route path=":id">
      <Route index element={<HkjPositionDetail />} />
      <Route path="edit" element={<HkjPositionUpdate />} />
      <Route path="delete" element={<HkjPositionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjPositionRoutes;
