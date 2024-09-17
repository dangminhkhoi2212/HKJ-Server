import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserExtra from './user-extra';
import HkjPosition from './hkj-position';
import HkjHire from './hkj-hire';
import HkjSalary from './hkj-salary';
import HkjEmployee from './hkj-employee';
import HkjProject from './hkj-project';
import HkjTask from './hkj-task';
import HkjCategory from './hkj-category';
import HkjJewelryModel from './hkj-jewelry-model';
import HkjJewelryImage from './hkj-jewelry-image';
import HkjTaskImage from './hkj-task-image';
import HkjTempImage from './hkj-temp-image';
import HkjOrder from './hkj-order';
import HkjTemplate from './hkj-template';
import HkjTemplateStep from './hkj-template-step';
import HkjMaterial from './hkj-material';
import HkjMaterialUsage from './hkj-material-usage';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="user-extra/*" element={<UserExtra />} />
        <Route path="hkj-position/*" element={<HkjPosition />} />
        <Route path="hkj-hire/*" element={<HkjHire />} />
        <Route path="hkj-salary/*" element={<HkjSalary />} />
        <Route path="hkj-employee/*" element={<HkjEmployee />} />
        <Route path="hkj-project/*" element={<HkjProject />} />
        <Route path="hkj-task/*" element={<HkjTask />} />
        <Route path="hkj-category/*" element={<HkjCategory />} />
        <Route path="hkj-jewelry-model/*" element={<HkjJewelryModel />} />
        <Route path="hkj-jewelry-image/*" element={<HkjJewelryImage />} />
        <Route path="hkj-task-image/*" element={<HkjTaskImage />} />
        <Route path="hkj-temp-image/*" element={<HkjTempImage />} />
        <Route path="hkj-order/*" element={<HkjOrder />} />
        <Route path="hkj-template/*" element={<HkjTemplate />} />
        <Route path="hkj-template-step/*" element={<HkjTemplateStep />} />
        <Route path="hkj-material/*" element={<HkjMaterial />} />
        <Route path="hkj-material-usage/*" element={<HkjMaterialUsage />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
