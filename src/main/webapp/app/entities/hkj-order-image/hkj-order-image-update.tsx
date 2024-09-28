import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getHkjOrders } from 'app/entities/hkj-order/hkj-order.reducer';
import { createEntity, getEntity, reset, updateEntity } from './hkj-order-image.reducer';

export const HkjOrderImageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hkjOrders = useAppSelector(state => state.hkjOrder.entities);
  const hkjOrderImageEntity = useAppSelector(state => state.hkjOrderImage.entity);
  const loading = useAppSelector(state => state.hkjOrderImage.loading);
  const updating = useAppSelector(state => state.hkjOrderImage.updating);
  const updateSuccess = useAppSelector(state => state.hkjOrderImage.updateSuccess);

  const handleClose = () => {
    navigate(`/hkj-order-image${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHkjOrders({}));
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
      ...hkjOrderImageEntity,
      ...values,
      hkjOrder: hkjOrders.find(it => it.id.toString() === values.hkjOrder?.toString()),
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
          ...hkjOrderImageEntity,
          createdDate: convertDateTimeFromServer(hkjOrderImageEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(hkjOrderImageEntity.lastModifiedDate),
          hkjOrder: hkjOrderImageEntity?.hkjOrder?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="serverApp.hkjOrderImage.home.createOrEditLabel" data-cy="HkjOrderImageCreateUpdateHeading">
            <Translate contentKey="serverApp.hkjOrderImage.home.createOrEditLabel">Create or edit a HkjOrderImage</Translate>
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
                  id="hkj-order-image-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('serverApp.hkjOrderImage.url')}
                id="hkj-order-image-url"
                name="url"
                data-cy="url"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrderImage.isDeleted')}
                id="hkj-order-image-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrderImage.createdBy')}
                id="hkj-order-image-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrderImage.createdDate')}
                id="hkj-order-image-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrderImage.lastModifiedBy')}
                id="hkj-order-image-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('serverApp.hkjOrderImage.lastModifiedDate')}
                id="hkj-order-image-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="hkj-order-image-hkjOrder"
                name="hkjOrder"
                data-cy="hkjOrder"
                label={translate('serverApp.hkjOrderImage.hkjOrder')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hkj-order-image" replace color="info">
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

export default HkjOrderImageUpdate;
