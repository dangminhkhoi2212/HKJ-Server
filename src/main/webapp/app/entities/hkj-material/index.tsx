import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjMaterial from './hkj-material';
import HkjMaterialDetail from './hkj-material-detail';
import HkjMaterialUpdate from './hkj-material-update';
import HkjMaterialDeleteDialog from './hkj-material-delete-dialog';

const HkjMaterialRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjMaterial />} />
    <Route path="new" element={<HkjMaterialUpdate />} />
    <Route path=":id">
      <Route index element={<HkjMaterialDetail />} />
      <Route path="edit" element={<HkjMaterialUpdate />} />
      <Route path="delete" element={<HkjMaterialDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjMaterialRoutes;
