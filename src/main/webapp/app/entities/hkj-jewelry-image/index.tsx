import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjJewelryImage from './hkj-jewelry-image';
import HkjJewelryImageDetail from './hkj-jewelry-image-detail';
import HkjJewelryImageUpdate from './hkj-jewelry-image-update';
import HkjJewelryImageDeleteDialog from './hkj-jewelry-image-delete-dialog';

const HkjJewelryImageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjJewelryImage />} />
    <Route path="new" element={<HkjJewelryImageUpdate />} />
    <Route path=":id">
      <Route index element={<HkjJewelryImageDetail />} />
      <Route path="edit" element={<HkjJewelryImageUpdate />} />
      <Route path="delete" element={<HkjJewelryImageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjJewelryImageRoutes;
