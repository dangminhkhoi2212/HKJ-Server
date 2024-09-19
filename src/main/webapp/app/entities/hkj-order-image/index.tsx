import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjOrderImage from './hkj-order-image';
import HkjOrderImageDetail from './hkj-order-image-detail';
import HkjOrderImageUpdate from './hkj-order-image-update';
import HkjOrderImageDeleteDialog from './hkj-order-image-delete-dialog';

const HkjOrderImageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjOrderImage />} />
    <Route path="new" element={<HkjOrderImageUpdate />} />
    <Route path=":id">
      <Route index element={<HkjOrderImageDetail />} />
      <Route path="edit" element={<HkjOrderImageUpdate />} />
      <Route path="delete" element={<HkjOrderImageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjOrderImageRoutes;
