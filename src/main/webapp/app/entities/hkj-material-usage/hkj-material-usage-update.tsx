import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getHkjMaterials } from 'app/entities/hkj-material/hkj-material.reducer';
import { getEntities as getHkjJewelryModels } from 'app/entities/hkj-jewelry-model/hkj-jewelry-model.reducer';
import { getEntities as getHkjTasks } from 'app/entities/hkj-task/hkj-task.reducer';
import { createEntity, getEntity, reset, updateEntity } from './hkj-material-usage.reducer';

export const HkjMaterialUsageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hkjMaterials = useAppSelector(state => state.hkjMaterial.entities);
  const hkjJewelryModels = useAppSelector(state => state.hkjJewelryModel.entities);
  const hkjTasks = useAppSelector(state => state.hkjTask.entities);
  const hkjMaterialUsageEntity = useAppSelector(state => state.hkjMaterialUsage.entity);
  const loading = useAppSelector(state => state.hkjMaterialUsage.loading);
  const updating = useAppSelector(state => state.hkjMaterialUsage.updating);
  const updateSuccess = useAppSelector(state => state.hkjMaterialUsage.updateSuccess);

  const handleClose = () => {
    navigate(`/hkj-material-usage${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHkjMaterials({}));
    dispatch(getHkjJewelryModels({}));
    dispatch(getHkjTasks({}));
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
    if (values.usage !== undefined && typeof values.usage !== 'number') {
      values.usage = Number(values.usage);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...hkjMaterialUsageEntity,
      ...values,
      material: hkjMaterials.find(it => it.id.toString() === values.material?.toString()),
      jewelry: hkjJewelryModels.find(it => it.id.toString() === values.jewelry?.toString()),
      task: hkjTasks.find(it => it.id.toString() === values.task?.toString()),
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
          ...hkjMaterialUsageEntity,
          createdDate: convertDateTimeFromServer(hkjMaterialUsageEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjMaterialUsageEntity.lastModifiedDate),
          material: hkjMaterialUsageEntity?.material?.id,
          jewelry: hkjMaterialUsageEntity?.jewelry?.id,
          task: hkjMaterialUsageEntity?.task?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjMaterialUsage.home.createOrEditLabel" data-cy="HkjMaterialUsageCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjMaterialUsage.home.createOrEditLabel">Create or edit a HkjMaterialUsage</Translate>
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
                  id="hkj-material-usage-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjMaterialUsage.usage')}
                id="hkj-material-usage-usage"
                name="usage"
                data-cy="usage"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterialUsage.notes')}
                id="hkj-material-usage-notes"
                name="notes"
                data-cy="notes"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterialUsage.price')}
                id="hkj-material-usage-price"
                name="price"
                data-cy="price"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterialUsage.isDeleted')}
                id="hkj-material-usage-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterialUsage.createdBy')}
                id="hkj-material-usage-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterialUsage.createdDate')}
                id="hkj-material-usage-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterialUsage.lastModifiedBy')}
                id="hkj-material-usage-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjMaterialUsage.lastModifiedDate')}
                id="hkj-material-usage-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-material-usage-material"
                name="material"
                data-cy="material"
                label={translate('serverApp.hkjMaterialUsage.material')}
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
              <ValidatedField
                id="hkj-material-usage-jewelry"
                name="jewelry"
                data-cy="jewelry"
                label={translate('serverApp.hkjMaterialUsage.jewelry')}
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
              <ValidatedField
                id="hkj-material-usage-task"
                name="task"
                data-cy="task"
                label={translate('serverApp.hkjMaterialUsage.task')}
                type="select"
              >
                <option value="" key="0" />
                {hkjTasks
                  ? hkjTasks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-material-usage" replace color="info">
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

export default HkjMaterialUsageUpdate;
