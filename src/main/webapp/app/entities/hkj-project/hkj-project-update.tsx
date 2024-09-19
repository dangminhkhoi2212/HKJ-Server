import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHkjTemplate } from 'app/shared/model/hkj-template.model';
import { getEntities as getHkjTemplates } from 'app/entities/hkj-template/hkj-template.reducer';
import { IHkjEmployee } from 'app/shared/model/hkj-employee.model';
import { getEntities as getHkjEmployees } from 'app/entities/hkj-employee/hkj-employee.reducer';
import { IHkjProject } from 'app/shared/model/hkj-project.model';
import { HkjOrderStatus } from 'app/shared/model/enumerations/hkj-order-status.model';
import { HkjPriority } from 'app/shared/model/enumerations/hkj-priority.model';
import { getEntity, updateEntity, createEntity, reset } from './hkj-project.reducer';

export const HkjProjectUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hkjTemplates = useAppSelector(state => state.hkjTemplate.entities);
  const hkjEmployees = useAppSelector(state => state.hkjEmployee.entities);
  const hkjProjectEntity = useAppSelector(state => state.hkjProject.entity);
  const loading = useAppSelector(state => state.hkjProject.loading);
  const updating = useAppSelector(state => state.hkjProject.updating);
  const updateSuccess = useAppSelector(state => state.hkjProject.updateSuccess);
  const hkjOrderStatusValues = Object.keys(HkjOrderStatus);
  const hkjPriorityValues = Object.keys(HkjPriority);

  const handleClose = () => {
    navigate('/hkj-project' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHkjTemplates({}));
    dispatch(getHkjEmployees({}));
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
    values.startDate = convertDateTimeToServer(values.startDate);
    values.expectDate = convertDateTimeToServer(values.expectDate);
    values.endDate = convertDateTimeToServer(values.endDate);
    if (values.budget !== undefined && typeof values.budget !== 'number') {
      values.budget = Number(values.budget);
    }
    if (values.actualCost !== undefined && typeof values.actualCost !== 'number') {
      values.actualCost = Number(values.actualCost);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...hkjProjectEntity,
      ...values,
      template: hkjTemplates.find(it => it.id.toString() === values.template?.toString()),
      manager: hkjEmployees.find(it => it.id.toString() === values.manager?.toString()),
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
          startDate: displayDefaultDateTime(),
          expectDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          status: 'NEW',
          priority: 'LOW',
          ...hkjProjectEntity,
          startDate: convertDateTimeFromServer(hkjProjectEntity.startDate),
          expectDate: convertDateTimeFromServer(hkjProjectEntity.expectDate),
          endDate: convertDateTimeFromServer(hkjProjectEntity.endDate),
          createdDate: convertDateTimeFromServer(hkjProjectEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjProjectEntity.lastModifiedDate),
          template: hkjProjectEntity?.template?.id,
          manager: hkjProjectEntity?.manager?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjProject.home.createOrEditLabel" data-cy="HkjProjectCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjProject.home.createOrEditLabel">Create or edit a HkjProject</Translate>
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
                  id="hkj-project-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjProject.name')}
                id="hkj-project-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjProject.description')}
                id="hkj-project-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjProject.startDate')}
                id="hkj-project-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjProject.expectDate')}
                id="hkj-project-expectDate"
                name="expectDate"
                data-cy="expectDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjProject.endDate')}
                id="hkj-project-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjProject.status')}
                id="hkj-project-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {hkjOrderStatusValues.map(hkjOrderStatus => (
                  <option value={hkjOrderStatus} key={hkjOrderStatus}>
                    {translate('serverApp.HkjOrderStatus.' + hkjOrderStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('serverApp.hkjProject.priority')}
                id="hkj-project-priority"
                name="priority"
                data-cy="priority"
                type="select"
              >
                {hkjPriorityValues.map(hkjPriority => (
                  <option value={hkjPriority} key={hkjPriority}>
                    {translate('serverApp.HkjPriority.' + hkjPriority)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('serverApp.hkjProject.budget')}
                id="hkj-project-budget"
                name="budget"
                data-cy="budget"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjProject.actualCost')}
                id="hkj-project-actualCost"
                name="actualCost"
                data-cy="actualCost"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjProject.qualityCheck')}
                id="hkj-project-qualityCheck"
                name="qualityCheck"
                data-cy="qualityCheck"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjProject.notes')}
                id="hkj-project-notes"
                name="notes"
                data-cy="notes"
                type="text"
                validate={{
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjProject.isDeleted')}
                id="hkj-project-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjProject.createdBy')}
                id="hkj-project-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjProject.createdDate')}
                id="hkj-project-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjProject.lastModifiedBy')}
                id="hkj-project-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjProject.lastModifiedDate')}
                id="hkj-project-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-project-template"
                name="template"
                data-cy="template"
                label={translate('serverApp.hkjProject.template')}
                type="select"
              >
                <option value="" key="0" />
                {hkjTemplates
                  ? hkjTemplates.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="hkj-project-manager"
                name="manager"
                data-cy="manager"
                label={translate('serverApp.hkjProject.manager')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-project" replace color="info">
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

export default HkjProjectUpdate;
