import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './hkj-material.reducer';

export const HkjMaterialUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hkjMaterialEntity = useAppSelector(state => state.hkjMaterial.entity);
  const loading = useAppSelector(state => state.hkjMaterial.loading);
  const updating = useAppSelector(state => state.hkjMaterial.updating);
  const updateSuccess = useAppSelector(state => state.hkjMaterial.updateSuccess);

  const handleClose = () => {
    navigate(`/hkj-material${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.pricePerUnit !== undefined && typeof values.pricePerUnit !== 'number') {
      values.pricePerUnit = Number(values.pricePerUnit);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...hkjMaterialEntity,
      ...values,
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
          ...hkjMaterialEntity,
          createdDate: convertDateTimeFromServer(hkjMaterialEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjMaterialEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjMaterial.home.createOrEditLabel" data-cy="HkjMaterialCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjMaterial.home.createOrEditLabel">Create or edit a HkjMaterial</Translate>
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
                  id="hkj-material-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjMaterial.name')}
                id="hkj-material-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterial.unit')}
                id="hkj-material-unit"
                name="unit"
                data-cy="unit"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterial.pricePerUnit')}
                id="hkj-material-pricePerUnit"
                name="pricePerUnit"
                data-cy="pricePerUnit"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterial.coverImage')}
                id="hkj-material-coverImage"
                name="coverImage"
                data-cy="coverImage"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterial.isDeleted')}
                id="hkj-material-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterial.createdBy')}
                id="hkj-material-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterial.createdDate')}
                id="hkj-material-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterial.lastModifiedBy')}
                id="hkj-material-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterial.lastModifiedDate')}
                id="hkj-material-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-material" replace color="info">
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

export default HkjMaterialUpdate;
