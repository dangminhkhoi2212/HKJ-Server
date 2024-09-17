import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HkjTemplateStep from './hkj-template-step';
import HkjTemplateStepDetail from './hkj-template-step-detail';
import HkjTemplateStepUpdate from './hkj-template-step-update';
import HkjTemplateStepDeleteDialog from './hkj-template-step-delete-dialog';

const HkjTemplateStepRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HkjTemplateStep />} />
    <Route path="new" element={<HkjTemplateStepUpdate />} />
    <Route path=":id">
      <Route index element={<HkjTemplateStepDetail />} />
      <Route path="edit" element={<HkjTemplateStepUpdate />} />
      <Route path="delete" element={<HkjTemplateStepDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HkjTemplateStepRoutes;
