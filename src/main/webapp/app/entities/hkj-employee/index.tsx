import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjEmployee from './hkj-employee';
import HkjEmployeeDetail from './hkj-employee-detail';
import HkjEmployeeUpdate from './hkj-employee-update';
import HkjEmployeeDeleteDialog from './hkj-employee-delete-dialog';

const HkjEmployeeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjEmployee />} />
    <Route path="new" element={<HkjEmployeeUpdate />} />
    <Route path=":id">
      <Route index element={<HkjEmployeeDetail />} />
      <Route path="edit" element={<HkjEmployeeUpdate />} />
      <Route path="delete" element={<HkjEmployeeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjEmployeeRoutes;
