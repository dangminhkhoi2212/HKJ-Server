import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getHkjCategories } from 'app/entities/hkj-category/hkj-category.reducer';
import { getEntities as getHkjProjects } from 'app/entities/hkj-project/hkj-project.reducer';
import { createEntity, getEntity, reset, updateEntity } from './hkj-jewelry-model.reducer';

export const HkjJewelryModelUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hkjCategories = useAppSelector(state => state.hkjCategory.entities);
  const hkjProjects = useAppSelector(state => state.hkjProject.entities);
  const hkjJewelryModelEntity = useAppSelector(state => state.hkjJewelryModel.entity);
  const loading = useAppSelector(state => state.hkjJewelryModel.loading);
  const updating = useAppSelector(state => state.hkjJewelryModel.updating);
  const updateSuccess = useAppSelector(state => state.hkjJewelryModel.updateSuccess);

  const handleClose = () => {
    navigate(`/hkj-jewelry-model${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHkjCategories({}));
    dispatch(getHkjProjects({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.weight !== undefined && typeof values.weight !== 'number') {
      values.weight = Number(values.weight);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...hkjJewelryModelEntity,
      ...values,
      category: hkjCategories.find(it => it.id.toString() === values.category?.toString()),
      project: hkjProjects.find(it => it.id.toString() === values.project?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...hkjJewelryModelEntity,
          createdDate: convertDateTimeFromServer(hkjJewelryModelEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjJewelryModelEntity.lastModifiedDate),
          category: hkjJewelryModelEntity?.category?.id,
          project: hkjJewelryModelEntity?.project?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjJewelryModel.home.createOrEditLabel" data-cy="HkjJewelryModelCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjJewelryModel.home.createOrEditLabel">Create or edit a HkjJewelryModel</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="hkj-jewelry-model-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjJewelryModel.name')}
                id="hkj-jewelry-model-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryModel.description')}
                id="hkj-jewelry-model-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryModel.coverImage')}
                id="hkj-jewelry-model-coverImage"
                name="coverImage"
                data-cy="coverImage"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryModel.isCustom')}
                id="hkj-jewelry-model-isCustom"
                name="isCustom"
                data-cy="isCustom"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryModel.weight')}
                id="hkj-jewelry-model-weight"
                name="weight"
                data-cy="weight"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryModel.price')}
                id="hkj-jewelry-model-price"
                name="price"
                data-cy="price"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryModel.color')}
                id="hkj-jewelry-model-color"
                name="color"
                data-cy="color"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryModel.notes')}
                id="hkj-jewelry-model-notes"
                name="notes"
                data-cy="notes"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryModel.isDeleted')}
                id="hkj-jewelry-model-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryModel.active')}
                id="hkj-jewelry-model-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryModel.createdBy')}
                id="hkj-jewelry-model-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryModel.createdDate')}
                id="hkj-jewelry-model-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryModel.lastModifiedBy')}
                id="hkj-jewelry-model-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryModel.lastModifiedDate')}
                id="hkj-jewelry-model-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-jewelry-model-category"
                name="category"
                data-cy="category"
                label={translate('serverApp.hkjJewelryModel.category')}
                type="select"
              >
                <option value="" key="0" />
                {hkjCategories
                  ? hkjCategories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="hkj-jewelry-model-project"
                name="project"
                data-cy="project"
                label={translate('serverApp.hkjJewelryModel.project')}
                type="select"
              >
                <option value="" key="0" />
                {hkjProjects
                  ? hkjProjects.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-jewelry-model" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default HkjJewelryModelUpdate;
