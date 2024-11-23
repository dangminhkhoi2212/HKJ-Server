import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getHkjMaterials } from 'app/entities/hkj-material/hkj-material.reducer';
import { getEntities as getHkjOrders } from 'app/entities/hkj-order/hkj-order.reducer';
import { getEntities as getHkjJewelryModels } from 'app/entities/hkj-jewelry-model/hkj-jewelry-model.reducer';
import { getEntities as getHkjCategories } from 'app/entities/hkj-category/hkj-category.reducer';
import { createEntity, getEntity, reset, updateEntity } from './hkj-order-item.reducer';

export const HkjOrderItemUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hkjMaterials = useAppSelector(state => state.hkjMaterial.entities);
  const hkjOrders = useAppSelector(state => state.hkjOrder.entities);
  const hkjJewelryModels = useAppSelector(state => state.hkjJewelryModel.entities);
  const hkjCategories = useAppSelector(state => state.hkjCategory.entities);
  const hkjOrderItemEntity = useAppSelector(state => state.hkjOrderItem.entity);
  const loading = useAppSelector(state => state.hkjOrderItem.loading);
  const updating = useAppSelector(state => state.hkjOrderItem.updating);
  const updateSuccess = useAppSelector(state => state.hkjOrderItem.updateSuccess);

  const handleClose = () => {
    navigate(`/hkj-order-item${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHkjMaterials({}));
    dispatch(getHkjOrders({}));
    dispatch(getHkjJewelryModels({}));
    dispatch(getHkjCategories({}));
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
    if (values.quantity !== undefined && typeof values.quantity !== 'number') {
      values.quantity = Number(values.quantity);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...hkjOrderItemEntity,
      ...values,
      material: hkjMaterials.find(it => it.id.toString() === values.material?.toString()),
      order: hkjOrders.find(it => it.id.toString() === values.order?.toString()),
      product: hkjJewelryModels.find(it => it.id.toString() === values.product?.toString()),
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
          ...hkjOrderItemEntity,
          createdDate: convertDateTimeFromServer(hkjOrderItemEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjOrderItemEntity.lastModifiedDate),
          material: hkjOrderItemEntity?.material?.id,
          order: hkjOrderItemEntity?.order?.id,
          product: hkjOrderItemEntity?.product?.id,
          category: hkjOrderItemEntity?.category?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjOrderItem.home.createOrEditLabel" data-cy="HkjOrderItemCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjOrderItem.home.createOrEditLabel">Create or edit a HkjOrderItem</Translate>
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
                  id="hkj-order-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjOrderItem.quantity')}
                id="hkj-order-item-quantity"
                name="quantity"
                data-cy="quantity"
                type="text"
                validate={{
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  max: { value: 20, message: translate('entity.validation.max', { max: 20 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjOrderItem.specialRequests')}
                id="hkj-order-item-specialRequests"
                name="specialRequests"
                data-cy="specialRequests"
                type="text"
                validate={{
                  maxLength: { value: 5000, message: translate('entity.validation.maxlength', { max: 5000 }) },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjOrderItem.price')}
                id="hkj-order-item-price"
                name="price"
                data-cy="price"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrderItem.isDeleted')}
                id="hkj-order-item-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrderItem.notes')}
                id="hkj-order-item-notes"
                name="notes"
                data-cy="notes"
                type="text"
                validate={{
                  maxLength: { value: 5000, message: translate('entity.validation.maxlength', { max: 5000 }) },
                }}
              />
              <ValidatedField
                label={translate('serverApp.hkjOrderItem.createdBy')}
                id="hkj-order-item-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrderItem.createdDate')}
                id="hkj-order-item-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrderItem.lastModifiedBy')}
                id="hkj-order-item-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrderItem.lastModifiedDate')}
                id="hkj-order-item-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-order-item-material"
                name="material"
                data-cy="material"
                label={translate('serverApp.hkjOrderItem.material')}
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
                id="hkj-order-item-order"
                name="order"
                data-cy="order"
                label={translate('serverApp.hkjOrderItem.order')}
                type="select"
              >
                <option value="" key="0" />
                {hkjOrders
                  ? hkjOrders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="hkj-order-item-product"
                name="product"
                data-cy="product"
                label={translate('serverApp.hkjOrderItem.product')}
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
                id="hkj-order-item-category"
                name="category"
                data-cy="category"
                label={translate('serverApp.hkjOrderItem.category')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-order-item" replace color="info">
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

export default HkjOrderItemUpdate;
