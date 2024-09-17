import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjTempImage from './hkj-temp-image';
import HkjTempImageDetail from './hkj-temp-image-detail';
import HkjTempImageUpdate from './hkj-temp-image-update';
import HkjTempImageDeleteDialog from './hkj-temp-image-delete-dialog';

const HkjTempImageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjTempImage />} />
    <Route path="new" element={<HkjTempImageUpdate />} />
    <Route path=":id">
      <Route index element={<HkjTempImageDetail />} />
      <Route path="edit" element={<HkjTempImageUpdate />} />
      <Route path="delete" element={<HkjTempImageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjTempImageRoutes;
