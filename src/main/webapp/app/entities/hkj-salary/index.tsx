import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjSalary from './hkj-salary';
import HkjSalaryDetail from './hkj-salary-detail';
import HkjSalaryUpdate from './hkj-salary-update';
import HkjSalaryDeleteDialog from './hkj-salary-delete-dialog';

const HkjSalaryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjSalary />} />
    <Route path="new" element={<HkjSalaryUpdate />} />
    <Route path=":id">
      <Route index element={<HkjSalaryDetail />} />
      <Route path="edit" element={<HkjSalaryUpdate />} />
      <Route path="delete" element={<HkjSalaryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjSalaryRoutes;
