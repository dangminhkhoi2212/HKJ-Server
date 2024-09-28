import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjMaterialImage from './hkj-material-image';
import HkjMaterialImageDetail from './hkj-material-image-detail';
import HkjMaterialImageUpdate from './hkj-material-image-update';
import HkjMaterialImageDeleteDialog from './hkj-material-image-delete-dialog';

const HkjMaterialImageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjMaterialImage />} />
    <Route path="new" element={<HkjMaterialImageUpdate />} />
    <Route path=":id">
      <Route index element={<HkjMaterialImageDetail />} />
      <Route path="edit" element={<HkjMaterialImageUpdate />} />
      <Route path="delete" element={<HkjMaterialImageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjMaterialImageRoutes;
