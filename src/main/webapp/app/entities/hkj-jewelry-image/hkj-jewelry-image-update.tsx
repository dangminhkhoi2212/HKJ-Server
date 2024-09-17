import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHkjEmployee } from 'app/shared/model/hkj-employee.model';
import { getEntities as getHkjEmployees } from 'app/entities/hkj-employee/hkj-employee.reducer';
import { IHkjJewelryModel } from 'app/shared/model/hkj-jewelry-model.model';
import { getEntities as getHkjJewelryModels } from 'app/entities/hkj-jewelry-model/hkj-jewelry-model.reducer';
import { IHkjJewelryImage } from 'app/shared/model/hkj-jewelry-image.model';
import { getEntity, updateEntity, createEntity, reset } from './hkj-jewelry-image.reducer';

export const HkjJewelryImageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hkjEmployees = useAppSelector(state => state.hkjEmployee.entities);
  const hkjJewelryModels = useAppSelector(state => state.hkjJewelryModel.entities);
  const hkjJewelryImageEntity = useAppSelector(state => state.hkjJewelryImage.entity);
  const loading = useAppSelector(state => state.hkjJewelryImage.loading);
  const updating = useAppSelector(state => state.hkjJewelryImage.updating);
  const updateSuccess = useAppSelector(state => state.hkjJewelryImage.updateSuccess);

  const handleClose = () => {
    navigate('/hkj-jewelry-image' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHkjEmployees({}));
    dispatch(getHkjJewelryModels({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...hkjJewelryImageEntity,
      ...values,
      uploadedBy: hkjEmployees.find(it => it.id.toString() === values.uploadedBy?.toString()),
      jewelryModel: hkjJewelryModels.find(it => it.id.toString() === values.jewelryModel?.toString()),
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
          ...hkjJewelryImageEntity,
          createdDate: convertDateTimeFromServer(hkjJewelryImageEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjJewelryImageEntity.lastModifiedDate),
          uploadedBy: hkjJewelryImageEntity?.uploadedBy?.id,
          jewelryModel: hkjJewelryImageEntity?.jewelryModel?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjJewelryImage.home.createOrEditLabel" data-cy="HkjJewelryImageCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjJewelryImage.home.createOrEditLabel">Create or edit a HkjJewelryImage</Translate>
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
                  id="hkj-jewelry-image-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjJewelryImage.url')}
                id="hkj-jewelry-image-url"
                name="url"
                data-cy="url"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryImage.isSearchImage')}
                id="hkj-jewelry-image-isSearchImage"
                name="isSearchImage"
                data-cy="isSearchImage"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryImage.description')}
                id="hkj-jewelry-image-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryImage.tags')}
                id="hkj-jewelry-image-tags"
                name="tags"
                data-cy="tags"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryImage.createdBy')}
                id="hkj-jewelry-image-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryImage.createdDate')}
                id="hkj-jewelry-image-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryImage.lastModifiedBy')}
                id="hkj-jewelry-image-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjJewelryImage.lastModifiedDate')}
                id="hkj-jewelry-image-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-jewelry-image-uploadedBy"
                name="uploadedBy"
                data-cy="uploadedBy"
                label={translate('serverApp.hkjJewelryImage.uploadedBy')}
                type="select"
              >
                <option value="" key="0" />
                {hkjEmployees
                  ? hkjEmployees.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="hkj-jewelry-image-jewelryModel"
                name="jewelryModel"
                data-cy="jewelryModel"
                label={translate('serverApp.hkjJewelryImage.jewelryModel')}
                type="select"
              >
                <option value="" key="0" />
                {hkjJewelryModels
                  ? hkjJewelryModels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-jewelry-image" replace color="info">
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

export default HkjJewelryImageUpdate;
