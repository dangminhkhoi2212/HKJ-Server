import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjCategory from './hkj-category';
import HkjCategoryDetail from './hkj-category-detail';
import HkjCategoryUpdate from './hkj-category-update';
import HkjCategoryDeleteDialog from './hkj-category-delete-dialog';

const HkjCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjCategory />} />
    <Route path="new" element={<HkjCategoryUpdate />} />
    <Route path=":id">
      <Route index element={<HkjCategoryDetail />} />
      <Route path="edit" element={<HkjCategoryUpdate />} />
      <Route path="delete" element={<HkjCategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjCategoryRoutes;
