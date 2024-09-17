import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjJewelryModel from './hkj-jewelry-model';
import HkjJewelryModelDetail from './hkj-jewelry-model-detail';
import HkjJewelryModelUpdate from './hkj-jewelry-model-update';
import HkjJewelryModelDeleteDialog from './hkj-jewelry-model-delete-dialog';

const HkjJewelryModelRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjJewelryModel />} />
    <Route path="new" element={<HkjJewelryModelUpdate />} />
    <Route path=":id">
      <Route index element={<HkjJewelryModelDetail />} />
      <Route path="edit" element={<HkjJewelryModelUpdate />} />
      <Route path="delete" element={<HkjJewelryModelDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjJewelryModelRoutes;
