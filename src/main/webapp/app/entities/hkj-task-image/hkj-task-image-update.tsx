import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getHkjTasks } from 'app/entities/hkj-task/hkj-task.reducer';
import { createEntity, getEntity, reset, updateEntity } from './hkj-task-image.reducer';

export const HkjTaskImageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hkjTasks = useAppSelector(state => state.hkjTask.entities);
  const hkjTaskImageEntity = useAppSelector(state => state.hkjTaskImage.entity);
  const loading = useAppSelector(state => state.hkjTaskImage.loading);
  const updating = useAppSelector(state => state.hkjTaskImage.updating);
  const updateSuccess = useAppSelector(state => state.hkjTaskImage.updateSuccess);

  const handleClose = () => {
    navigate(`/hkj-task-image${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...hkjTaskImageEntity,
      ...values,
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
          ...hkjTaskImageEntity,
          createdDate: convertDateTimeFromServer(hkjTaskImageEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjTaskImageEntity.lastModifiedDate),
          task: hkjTaskImageEntity?.task?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjTaskImage.home.createOrEditLabel" data-cy="HkjTaskImageCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjTaskImage.home.createOrEditLabel">Create or edit a HkjTaskImage</Translate>
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
                  id="hkj-task-image-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjTaskImage.url')}
                id="hkj-task-image-url"
                name="url"
                data-cy="url"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjTaskImage.description')}
                id="hkj-task-image-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTaskImage.isDeleted')}
                id="hkj-task-image-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjTaskImage.createdBy')}
                id="hkj-task-image-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTaskImage.createdDate')}
                id="hkj-task-image-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjTaskImage.lastModifiedBy')}
                id="hkj-task-image-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTaskImage.lastModifiedDate')}
                id="hkj-task-image-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-task-image-task"
                name="task"
                data-cy="task"
                label={translate('serverApp.hkjTaskImage.task')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-task-image" replace color="info">
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

export default HkjTaskImageUpdate;
