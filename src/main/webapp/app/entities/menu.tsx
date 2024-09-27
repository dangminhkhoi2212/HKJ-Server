import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/user-extra">
        <Translate contentKey="global.menu.entities.userExtra" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-position">
        <Translate contentKey="global.menu.entities.hkjPosition" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-hire">
        <Translate contentKey="global.menu.entities.hkjHire" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-salary">
        <Translate contentKey="global.menu.entities.hkjSalary" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-project">
        <Translate contentKey="global.menu.entities.hkjProject" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-task">
        <Translate contentKey="global.menu.entities.hkjTask" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-category">
        <Translate contentKey="global.menu.entities.hkjCategory" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-jewelry-model">
        <Translate contentKey="global.menu.entities.hkjJewelryModel" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-jewelry-image">
        <Translate contentKey="global.menu.entities.hkjJewelryImage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-task-image">
        <Translate contentKey="global.menu.entities.hkjTaskImage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-temp-image">
        <Translate contentKey="global.menu.entities.hkjTempImage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-order">
        <Translate contentKey="global.menu.entities.hkjOrder" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-template">
        <Translate contentKey="global.menu.entities.hkjTemplate" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-template-step">
        <Translate contentKey="global.menu.entities.hkjTemplateStep" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-material">
        <Translate contentKey="global.menu.entities.hkjMaterial" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-material-usage">
        <Translate contentKey="global.menu.entities.hkjMaterialUsage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hkj-order-image">
        <Translate contentKey="global.menu.entities.hkjOrderImage" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
