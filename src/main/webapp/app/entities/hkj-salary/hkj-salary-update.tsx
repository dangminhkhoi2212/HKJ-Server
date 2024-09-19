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
import { IHkjSalary } from 'app/shared/model/hkj-salary.model';
import { getEntity, updateEntity, createEntity, reset } from './hkj-salary.reducer';

export const HkjSalaryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hkjEmployees = useAppSelector(state => state.hkjEmployee.entities);
  const hkjSalaryEntity = useAppSelector(state => state.hkjSalary.entity);
  const loading = useAppSelector(state => state.hkjSalary.loading);
  const updating = useAppSelector(state => state.hkjSalary.updating);
  const updateSuccess = useAppSelector(state => state.hkjSalary.updateSuccess);

  const handleClose = () => {
    navigate('/hkj-salary' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    if (values.salary !== undefined && typeof values.salary !== 'number') {
      values.salary = Number(values.salary);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...hkjSalaryEntity,
      ...values,
      hkjEmployee: hkjEmployees.find(it => it.id.toString() === values.hkjEmployee?.toString()),
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
          ...hkjSalaryEntity,
          createdDate: convertDateTimeFromServer(hkjSalaryEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjSalaryEntity.lastModifiedDate),
          hkjEmployee: hkjSalaryEntity?.hkjEmployee?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjSalary.home.createOrEditLabel" data-cy="HkjSalaryCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjSalary.home.createOrEditLabel">Create or edit a HkjSalary</Translate>
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
                  id="hkj-salary-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjSalary.salary')}
                id="hkj-salary-salary"
                name="salary"
                data-cy="salary"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjSalary.isDeleted')}
                id="hkj-salary-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjSalary.createdBy')}
                id="hkj-salary-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjSalary.createdDate')}
                id="hkj-salary-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjSalary.lastModifiedBy')}
                id="hkj-salary-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjSalary.lastModifiedDate')}
                id="hkj-salary-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-salary-hkjEmployee"
                name="hkjEmployee"
                data-cy="hkjEmployee"
                label={translate('serverApp.hkjSalary.hkjEmployee')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-salary" replace color="info">
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

export default HkjSalaryUpdate;
