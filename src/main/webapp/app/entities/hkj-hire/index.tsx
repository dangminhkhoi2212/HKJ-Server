import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjHire from './hkj-hire';
import HkjHireDetail from './hkj-hire-detail';
import HkjHireUpdate from './hkj-hire-update';
import HkjHireDeleteDialog from './hkj-hire-delete-dialog';

const HkjHireRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjHire />} />
    <Route path="new" element={<HkjHireUpdate />} />
    <Route path=":id">
      <Route index element={<HkjHireDetail />} />
      <Route path="edit" element={<HkjHireUpdate />} />
      <Route path="delete" element={<HkjHireDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjHireRoutes;
