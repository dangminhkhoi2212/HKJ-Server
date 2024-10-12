import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjTrackSearchImage from './hkj-track-search-image';
import HkjTrackSearchImageDetail from './hkj-track-search-image-detail';
import HkjTrackSearchImageUpdate from './hkj-track-search-image-update';
import HkjTrackSearchImageDeleteDialog from './hkj-track-search-image-delete-dialog';

const HkjTrackSearchImageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjTrackSearchImage />} />
    <Route path="new" element={<HkjTrackSearchImageUpdate />} />
    <Route path=":id">
      <Route index element={<HkjTrackSearchImageDetail />} />
      <Route path="edit" element={<HkjTrackSearchImageUpdate />} />
      <Route path="delete" element={<HkjTrackSearchImageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjTrackSearchImageRoutes;
