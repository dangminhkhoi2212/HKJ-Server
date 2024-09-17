import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjProject from './hkj-project';
import HkjProjectDetail from './hkj-project-detail';
import HkjProjectUpdate from './hkj-project-update';
import HkjProjectDeleteDialog from './hkj-project-delete-dialog';

const HkjProjectRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjProject />} />
    <Route path="new" element={<HkjProjectUpdate />} />
    <Route path=":id">
      <Route index element={<HkjProjectDetail />} />
      <Route path="edit" element={<HkjProjectUpdate />} />
      <Route path="delete" element={<HkjProjectDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjProjectRoutes;
