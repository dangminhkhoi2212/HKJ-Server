import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjMaterialUsage from './hkj-material-usage';
import HkjMaterialUsageDetail from './hkj-material-usage-detail';
import HkjMaterialUsageUpdate from './hkj-material-usage-update';
import HkjMaterialUsageDeleteDialog from './hkj-material-usage-delete-dialog';

const HkjMaterialUsageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjMaterialUsage />} />
    <Route path="new" element={<HkjMaterialUsageUpdate />} />
    <Route path=":id">
      <Route index element={<HkjMaterialUsageDetail />} />
      <Route path="edit" element={<HkjMaterialUsageUpdate />} />
      <Route path="delete" element={<HkjMaterialUsageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjMaterialUsageRoutes;
