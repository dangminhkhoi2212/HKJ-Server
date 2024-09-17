import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHkjCategory } from 'app/shared/model/hkj-category.model';
import { getEntities as getHkjCategories } from 'app/entities/hkj-category/hkj-category.reducer';
import { IHkjTemplate } from 'app/shared/model/hkj-template.model';
import { getEntity, updateEntity, createEntity, reset } from './hkj-template.reducer';

export const HkjTemplateUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hkjCategories = useAppSelector(state => state.hkjCategory.entities);
  const hkjTemplateEntity = useAppSelector(state => state.hkjTemplate.entity);
  const loading = useAppSelector(state => state.hkjTemplate.loading);
  const updating = useAppSelector(state => state.hkjTemplate.updating);
  const updateSuccess = useAppSelector(state => state.hkjTemplate.updateSuccess);

  const handleClose = () => {
    navigate('/hkj-template' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHkjCategories({}));
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
      ...hkjTemplateEntity,
      ...values,
      category: hkjCategories.find(it => it.id.toString() === values.category?.toString()),
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
          ...hkjTemplateEntity,
          createdDate: convertDateTimeFromServer(hkjTemplateEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjTemplateEntity.lastModifiedDate),
          category: hkjTemplateEntity?.category?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjTemplate.home.createOrEditLabel" data-cy="HkjTemplateCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjTemplate.home.createOrEditLabel">Create or edit a HkjTemplate</Translate>
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
                  id="hkj-template-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjTemplate.name')}
                id="hkj-template-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTemplate.createdBy')}
                id="hkj-template-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTemplate.createdDate')}
                id="hkj-template-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjTemplate.lastModifiedBy')}
                id="hkj-template-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjTemplate.lastModifiedDate')}
                id="hkj-template-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-template-category"
                name="category"
                data-cy="category"
                label={translate('serverApp.hkjTemplate.category')}
                type="select"
              >
                <option value="" key="0" />
                {hkjCategories
                  ? hkjCategories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-template" replace color="info">
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

export default HkjTemplateUpdate;
