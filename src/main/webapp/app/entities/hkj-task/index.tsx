import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjTask from './hkj-task';
import HkjTaskDetail from './hkj-task-detail';
import HkjTaskUpdate from './hkj-task-update';
import HkjTaskDeleteDialog from './hkj-task-delete-dialog';

const HkjTaskRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjTask />} />
    <Route path="new" element={<HkjTaskUpdate />} />
    <Route path=":id">
      <Route index element={<HkjTaskDetail />} />
      <Route path="edit" element={<HkjTaskUpdate />} />
      <Route path="delete" element={<HkjTaskDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjTaskRoutes;
