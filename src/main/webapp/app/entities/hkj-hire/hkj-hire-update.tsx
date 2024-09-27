import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHkjPosition } from 'app/shared/model/hkj-position.model';
import { getEntities as getHkjPositions } from 'app/entities/hkj-position/hkj-position.reducer';
import { IUserExtra } from 'app/shared/model/user-extra.model';
import { getEntities as getUserExtras } from 'app/entities/user-extra/user-extra.reducer';
import { IHkjHire } from 'app/shared/model/hkj-hire.model';
import { getEntity, updateEntity, createEntity, reset } from './hkj-hire.reducer';

export const HkjHireUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hkjPositions = useAppSelector(state => state.hkjPosition.entities);
  const userExtras = useAppSelector(state => state.userExtra.entities);
  const hkjHireEntity = useAppSelector(state => state.hkjHire.entity);
  const loading = useAppSelector(state => state.hkjHire.loading);
  const updating = useAppSelector(state => state.hkjHire.updating);
  const updateSuccess = useAppSelector(state => state.hkjHire.updateSuccess);

  const handleClose = () => {
    navigate('/hkj-hire' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHkjPositions({}));
    dispatch(getUserExtras({}));
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
    values.beginDate = convertDateTimeToServer(values.beginDate);
    values.endDate = convertDateTimeToServer(values.endDate);
    if (values.beginSalary !== undefined && typeof values.beginSalary !== 'number') {
      values.beginSalary = Number(values.beginSalary);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...hkjHireEntity,
      ...values,
      position: hkjPositions.find(it => it.id.toString() === values.position?.toString()),
      employee: userExtras.find(it => it.id.toString() === values.employee?.toString()),
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
          beginDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...hkjHireEntity,
          beginDate: convertDateTimeFromServer(hkjHireEntity.beginDate),
          endDate: convertDateTimeFromServer(hkjHireEntity.endDate),
          createdDate: convertDateTimeFromServer(hkjHireEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjHireEntity.lastModifiedDate),
          position: hkjHireEntity?.position?.id,
          employee: hkjHireEntity?.employee?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjHire.home.createOrEditLabel" data-cy="HkjHireCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjHire.home.createOrEditLabel">Create or edit a HkjHire</Translate>
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
                  id="hkj-hire-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjHire.beginDate')}
                id="hkj-hire-beginDate"
                name="beginDate"
                data-cy="beginDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjHire.endDate')}
                id="hkj-hire-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjHire.beginSalary')}
                id="hkj-hire-beginSalary"
                name="beginSalary"
                data-cy="beginSalary"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjHire.isDeleted')}
                id="hkj-hire-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjHire.createdBy')}
                id="hkj-hire-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjHire.createdDate')}
                id="hkj-hire-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjHire.lastModifiedBy')}
                id="hkj-hire-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjHire.lastModifiedDate')}
                id="hkj-hire-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-hire-position"
                name="position"
                data-cy="position"
                label={translate('serverApp.hkjHire.position')}
                type="select"
              >
                <option value="" key="0" />
                {hkjPositions
                  ? hkjPositions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="hkj-hire-employee"
                name="employee"
                data-cy="employee"
                label={translate('serverApp.hkjHire.employee')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-hire" replace color="info">
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

export default HkjHireUpdate;
