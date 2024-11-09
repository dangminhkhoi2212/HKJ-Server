import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserExtras } from 'app/entities/user-extra/user-extra.reducer';
import { getEntities as getHkjProjects } from 'app/entities/hkj-project/hkj-project.reducer';
import { HkjOrderStatus } from 'app/shared/model/enumerations/hkj-order-status.model';
import { HkjPriority } from 'app/shared/model/enumerations/hkj-priority.model';
import { createEntity, getEntity, reset, updateEntity } from './hkj-task.reducer';

export const HkjTaskUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userExtras = useAppSelector(state => state.userExtra.entities);
  const hkjProjects = useAppSelector(state => state.hkjProject.entities);
  const hkjTaskEntity = useAppSelector(state => state.hkjTask.entity);
  const loading = useAppSelector(state => state.hkjTask.loading);
  const updating = useAppSelector(state => state.hkjTask.updating);
  const updateSuccess = useAppSelector(state => state.hkjTask.updateSuccess);
  const hkjOrderStatusValues = Object.keys(HkjOrderStatus);
  const hkjPriorityValues = Object.keys(HkjPriority);

  const handleClose = () => {
    navigate(`/hkj-task${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserExtras({}));
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
    values.assignedDate = convertDateTimeToServer(values.assignedDate);
    values.expectDate = convertDateTimeToServer(values.expectDate);
    values.completedDate = convertDateTimeToServer(values.completedDate);
    if (values.point !== undefined && typeof values.point !== 'number') {
      values.point = Number(values.point);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...hkjTaskEntity,
      ...values,
      employee: userExtras.find(it => it.id.toString() === values.employee?.toString()),
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
          assignedDate: displayDefaultDateTime(),
          expectDate: displayDefaultDateTime(),
          completedDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          status: 'NEW',
          priority: 'LOW',
          ...hkjTaskEntity,
          assignedDate: convertDateTimeFromServer(hkjTaskEntity.assignedDate),
          expectDate: convertDateTimeFromServer(hkjTaskEntity.expectDate),
          completedDate: convertDateTimeFromServer(hkjTaskEntity.completedDate),
          createdDate: convertDateTimeFromServer(hkjTaskEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjTaskEntity.lastModifiedDate),
          employee: hkjTaskEntity?.employee?.id,
          project: hkjTaskEntity?.project?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjTask.home.createOrEditLabel" data-cy="HkjTaskCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjTask.home.createOrEditLabel">Create or edit a HkjTask</Translate>
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
                  id="hkj-task-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjTask.name')}
                id="hkj-task-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjTask.coverImage')}
                id="hkj-task-coverImage"
                name="coverImage"
                data-cy="coverImage"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTask.description')}
                id="hkj-task-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 10000, message: translate('entity.validation.maxlength', { max: 10000 }) },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjTask.assignedDate')}
                id="hkj-task-assignedDate"
                name="assignedDate"
                data-cy="assignedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjTask.expectDate')}
                id="hkj-task-expectDate"
                name="expectDate"
                data-cy="expectDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjTask.completedDate')}
                id="hkj-task-completedDate"
                name="completedDate"
                data-cy="completedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjTask.status')}
                id="hkj-task-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {hkjOrderStatusValues.map(hkjOrderStatus => (
                  <option value={hkjOrderStatus} key={hkjOrderStatus}>
                    {translate(`serverApp.HkjOrderStatus.${hkjOrderStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('serverApp.hkjTask.priority')}
                id="hkj-task-priority"
                name="priority"
                data-cy="priority"
                type="select"
              >
                {hkjPriorityValues.map(hkjPriority => (
                  <option value={hkjPriority} key={hkjPriority}>
                    {translate(`serverApp.HkjPriority.${hkjPriority}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('serverApp.hkjTask.point')}
                id="hkj-task-point"
                name="point"
                data-cy="point"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  max: { value: 100, message: translate('entity.validation.max', { max: 100 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjTask.isDeleted')}
                id="hkj-task-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjTask.createdBy')}
                id="hkj-task-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTask.createdDate')}
                id="hkj-task-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjTask.lastModifiedBy')}
                id="hkj-task-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTask.lastModifiedDate')}
                id="hkj-task-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-task-employee"
                name="employee"
                data-cy="employee"
                label={translate('serverApp.hkjTask.employee')}
                type="select"
              >
                <option value="" key="0" />
                {userExtras
                  ? userExtras.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="hkj-task-project"
                name="project"
                data-cy="project"
                label={translate('serverApp.hkjTask.project')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-task" replace color="info">
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

export default HkjTaskUpdate;
