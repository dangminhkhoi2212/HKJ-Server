import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getHkjMaterials } from 'app/entities/hkj-material/hkj-material.reducer';
import { createEntity, getEntity, reset, updateEntity } from './hkj-material-image.reducer';

export const HkjMaterialImageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hkjMaterials = useAppSelector(state => state.hkjMaterial.entities);
  const hkjMaterialImageEntity = useAppSelector(state => state.hkjMaterialImage.entity);
  const loading = useAppSelector(state => state.hkjMaterialImage.loading);
  const updating = useAppSelector(state => state.hkjMaterialImage.updating);
  const updateSuccess = useAppSelector(state => state.hkjMaterialImage.updateSuccess);

  const handleClose = () => {
    navigate(`/hkj-material-image${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHkjMaterials({}));
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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...hkjMaterialImageEntity,
      ...values,
      material: hkjMaterials.find(it => it.id.toString() === values.material?.toString()),
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
          ...hkjMaterialImageEntity,
          createdDate: convertDateTimeFromServer(hkjMaterialImageEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjMaterialImageEntity.lastModifiedDate),
          material: hkjMaterialImageEntity?.material?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjMaterialImage.home.createOrEditLabel" data-cy="HkjMaterialImageCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjMaterialImage.home.createOrEditLabel">Create or edit a HkjMaterialImage</Translate>
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
                  id="hkj-material-image-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjMaterialImage.url')}
                id="hkj-material-image-url"
                name="url"
                data-cy="url"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterialImage.isDeleted')}
                id="hkj-material-image-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterialImage.createdBy')}
                id="hkj-material-image-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterialImage.createdDate')}
                id="hkj-material-image-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterialImage.lastModifiedBy')}
                id="hkj-material-image-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterialImage.lastModifiedDate')}
                id="hkj-material-image-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-material-image-material"
                name="material"
                data-cy="material"
                label={translate('serverApp.hkjMaterialImage.material')}
                type="select"
              >
                <option value="" key="0" />
                {hkjMaterials
                  ? hkjMaterials.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-material-image" replace color="info">
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

export default HkjMaterialImageUpdate;
