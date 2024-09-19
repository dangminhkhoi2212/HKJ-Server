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
import { IHkjTemplateStep } from 'app/shared/model/hkj-template-step.model';
import { getEntity, updateEntity, createEntity, reset } from './hkj-template-step.reducer';

export const HkjTemplateStepUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hkjTemplates = useAppSelector(state => state.hkjTemplate.entities);
  const hkjTemplateStepEntity = useAppSelector(state => state.hkjTemplateStep.entity);
  const loading = useAppSelector(state => state.hkjTemplateStep.loading);
  const updating = useAppSelector(state => state.hkjTemplateStep.updating);
  const updateSuccess = useAppSelector(state => state.hkjTemplateStep.updateSuccess);

  const handleClose = () => {
    navigate('/hkj-template-step' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHkjTemplates({}));
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
      ...hkjTemplateStepEntity,
      ...values,
      hkjTemplate: hkjTemplates.find(it => it.id.toString() === values.hkjTemplate?.toString()),
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
          ...hkjTemplateStepEntity,
          createdDate: convertDateTimeFromServer(hkjTemplateStepEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjTemplateStepEntity.lastModifiedDate),
          hkjTemplate: hkjTemplateStepEntity?.hkjTemplate?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjTemplateStep.home.createOrEditLabel" data-cy="HkjTemplateStepCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjTemplateStep.home.createOrEditLabel">Create or edit a HkjTemplateStep</Translate>
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
                  id="hkj-template-step-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjTemplateStep.name')}
                id="hkj-template-step-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTemplateStep.isDeleted')}
                id="hkj-template-step-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjTemplateStep.createdBy')}
                id="hkj-template-step-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTemplateStep.createdDate')}
                id="hkj-template-step-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjTemplateStep.lastModifiedBy')}
                id="hkj-template-step-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTemplateStep.lastModifiedDate')}
                id="hkj-template-step-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-template-step-hkjTemplate"
                name="hkjTemplate"
                data-cy="hkjTemplate"
                label={translate('serverApp.hkjTemplateStep.hkjTemplate')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-template-step" replace color="info">
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

export default HkjTemplateStepUpdate;
