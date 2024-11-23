import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserExtras } from 'app/entities/user-extra/user-extra.reducer';
import { getEntities as getHkjProjects } from 'app/entities/hkj-project/hkj-project.reducer';
import { HkjOrderStatus } from 'app/shared/model/enumerations/hkj-order-status.model';
import { createEntity, getEntity, reset, updateEntity } from './hkj-order.reducer';

export const HkjOrderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userExtras = useAppSelector(state => state.userExtra.entities);
  const hkjProjects = useAppSelector(state => state.hkjProject.entities);
  const hkjOrderEntity = useAppSelector(state => state.hkjOrder.entity);
  const loading = useAppSelector(state => state.hkjOrder.loading);
  const updating = useAppSelector(state => state.hkjOrder.updating);
  const updateSuccess = useAppSelector(state => state.hkjOrder.updateSuccess);
  const hkjOrderStatusValues = Object.keys(HkjOrderStatus);

  const handleClose = () => {
    navigate(`/hkj-order${location.search}`);
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
    values.orderDate = convertDateTimeToServer(values.orderDate);
    values.expectedDeliveryDate = convertDateTimeToServer(values.expectedDeliveryDate);
    values.actualDeliveryDate = convertDateTimeToServer(values.actualDeliveryDate);
    if (values.totalPrice !== undefined && typeof values.totalPrice !== 'number') {
      values.totalPrice = Number(values.totalPrice);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...hkjOrderEntity,
      ...values,
      customer: userExtras.find(it => it.id.toString() === values.customer?.toString()),
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
          customer: hkjOrderEntity?.customer?.id,
          project: hkjOrderEntity?.project?.id,
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
                label={translate('serverApp.hkjOrder.status')}
                id="hkj-order-status"
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
                label={translate('serverApp.hkjOrder.totalPrice')}
                id="hkj-order-totalPrice"
                name="totalPrice"
                data-cy="totalPrice"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrder.isDeleted')}
                id="hkj-order-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
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
