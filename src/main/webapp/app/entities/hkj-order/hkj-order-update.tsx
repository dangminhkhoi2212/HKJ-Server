import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHkjProject } from 'app/shared/model/hkj-project.model';
import { getEntities as getHkjProjects } from 'app/entities/hkj-project/hkj-project.reducer';
import { IHkjJewelryModel } from 'app/shared/model/hkj-jewelry-model.model';
import { getEntities as getHkjJewelryModels } from 'app/entities/hkj-jewelry-model/hkj-jewelry-model.reducer';
import { IUserExtra } from 'app/shared/model/user-extra.model';
import { getEntities as getUserExtras } from 'app/entities/user-extra/user-extra.reducer';
import { IHkjOrder } from 'app/shared/model/hkj-order.model';
import { HkjOrderStatus } from 'app/shared/model/enumerations/hkj-order-status.model';
import { getEntity, updateEntity, createEntity, reset } from './hkj-order.reducer';

export const HkjOrderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hkjProjects = useAppSelector(state => state.hkjProject.entities);
  const hkjJewelryModels = useAppSelector(state => state.hkjJewelryModel.entities);
  const userExtras = useAppSelector(state => state.userExtra.entities);
  const hkjOrderEntity = useAppSelector(state => state.hkjOrder.entity);
  const loading = useAppSelector(state => state.hkjOrder.loading);
  const updating = useAppSelector(state => state.hkjOrder.updating);
  const updateSuccess = useAppSelector(state => state.hkjOrder.updateSuccess);
  const hkjOrderStatusValues = Object.keys(HkjOrderStatus);

  const handleClose = () => {
    navigate('/hkj-order' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHkjProjects({}));
    dispatch(getHkjJewelryModels({}));
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
    values.orderDate = convertDateTimeToServer(values.orderDate);
    values.expectedDeliveryDate = convertDateTimeToServer(values.expectedDeliveryDate);
    values.actualDeliveryDate = convertDateTimeToServer(values.actualDeliveryDate);
    if (values.customerRating !== undefined && typeof values.customerRating !== 'number') {
      values.customerRating = Number(values.customerRating);
    }
    if (values.totalPrice !== undefined && typeof values.totalPrice !== 'number') {
      values.totalPrice = Number(values.totalPrice);
    }
    if (values.depositAmount !== undefined && typeof values.depositAmount !== 'number') {
      values.depositAmount = Number(values.depositAmount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...hkjOrderEntity,
      ...values,
      project: hkjProjects.find(it => it.id.toString() === values.project?.toString()),
      customOrder: hkjJewelryModels.find(it => it.id.toString() === values.customOrder?.toString()),
      customer: userExtras.find(it => it.id.toString() === values.customer?.toString()),
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
          orderDate: displayDefaultDateTime(),
          expectedDeliveryDate: displayDefaultDateTime(),
          actualDeliveryDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          status: 'NEW',
          ...hkjOrderEntity,
          orderDate: convertDateTimeFromServer(hkjOrderEntity.orderDate),
          expectedDeliveryDate: convertDateTimeFromServer(hkjOrderEntity.expectedDeliveryDate),
          actualDeliveryDate: convertDateTimeFromServer(hkjOrderEntity.actualDeliveryDate),
          createdDate: convertDateTimeFromServer(hkjOrderEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjOrderEntity.lastModifiedDate),
          project: hkjOrderEntity?.project?.id,
          customOrder: hkjOrderEntity?.customOrder?.id,
          customer: hkjOrderEntity?.customer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjOrder.home.createOrEditLabel" data-cy="HkjOrderCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjOrder.home.createOrEditLabel">Create or edit a HkjOrder</Translate>
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
                  id="hkj-order-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjOrder.orderDate')}
                id="hkj-order-orderDate"
                name="orderDate"
                data-cy="orderDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjOrder.expectedDeliveryDate')}
                id="hkj-order-expectedDeliveryDate"
                name="expectedDeliveryDate"
                data-cy="expectedDeliveryDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrder.actualDeliveryDate')}
                id="hkj-order-actualDeliveryDate"
                name="actualDeliveryDate"
                data-cy="actualDeliveryDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrder.specialRequests')}
                id="hkj-order-specialRequests"
                name="specialRequests"
                data-cy="specialRequests"
                type="text"
                validate={{
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjOrder.status')}
                id="hkj-order-status"
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
                label={translate('serverApp.hkjOrder.customerRating')}
                id="hkj-order-customerRating"
                name="customerRating"
                data-cy="customerRating"
                type="text"
                validate={{
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  max: { value: 5, message: translate('entity.validation.max', { max: 5 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjOrder.totalPrice')}
                id="hkj-order-totalPrice"
                name="totalPrice"
                data-cy="totalPrice"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrder.depositAmount')}
                id="hkj-order-depositAmount"
                name="depositAmount"
                data-cy="depositAmount"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrder.notes')}
                id="hkj-order-notes"
                name="notes"
                data-cy="notes"
                type="text"
                validate={{
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjOrder.createdBy')}
                id="hkj-order-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrder.createdDate')}
                id="hkj-order-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrder.lastModifiedBy')}
                id="hkj-order-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrder.lastModifiedDate')}
                id="hkj-order-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-order-project"
                name="project"
                data-cy="project"
                label={translate('serverApp.hkjOrder.project')}
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
              <ValidatedField
                id="hkj-order-customOrder"
                name="customOrder"
                data-cy="customOrder"
                label={translate('serverApp.hkjOrder.customOrder')}
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
                id="hkj-order-customer"
                name="customer"
                data-cy="customer"
                label={translate('serverApp.hkjOrder.customer')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-order" replace color="info">
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

export default HkjOrderUpdate;
