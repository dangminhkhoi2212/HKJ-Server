import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjTaskImage from './hkj-task-image';
import HkjTaskImageDetail from './hkj-task-image-detail';
import HkjTaskImageUpdate from './hkj-task-image-update';
import HkjTaskImageDeleteDialog from './hkj-task-image-delete-dialog';

const HkjTaskImageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjTaskImage />} />
    <Route path="new" element={<HkjTaskImageUpdate />} />
    <Route path=":id">
      <Route index element={<HkjTaskImageDetail />} />
      <Route path="edit" element={<HkjTaskImageUpdate />} />
      <Route path="delete" element={<HkjTaskImageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjTaskImageRoutes;
