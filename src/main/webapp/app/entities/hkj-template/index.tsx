import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjTemplate from './hkj-template';
import HkjTemplateDetail from './hkj-template-detail';
import HkjTemplateUpdate from './hkj-template-update';
import HkjTemplateDeleteDialog from './hkj-template-delete-dialog';

const HkjTemplateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjTemplate />} />
    <Route path="new" element={<HkjTemplateUpdate />} />
    <Route path=":id">
      <Route index element={<HkjTemplateDetail />} />
      <Route path="edit" element={<HkjTemplateUpdate />} />
      <Route path="delete" element={<HkjTemplateDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjTemplateRoutes;
