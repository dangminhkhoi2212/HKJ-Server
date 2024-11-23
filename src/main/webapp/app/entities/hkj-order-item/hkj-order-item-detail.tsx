import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-order-item.reducer';

export const HkjOrderItemDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjOrderItemEntity = useAppSelector(state => state.hkjOrderItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjOrderItemDetailsHeading">
          <Translate contentKey="serverApp.hkjOrderItem.detail.title">HkjOrderItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjOrderItemEntity.id}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="serverApp.hkjOrderItem.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{hkjOrderItemEntity.quantity}</dd>
          <dt>
            <span id="specialRequests">
              <Translate contentKey="serverApp.hkjOrderItem.specialRequests">Special Requests</Translate>
            </span>
          </dt>
          <dd>{hkjOrderItemEntity.specialRequests}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="serverApp.hkjOrderItem.price">Price</Translate>
            </span>
          </dt>
          <dd>{hkjOrderItemEntity.price}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.hkjOrderItem.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{hkjOrderItemEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="serverApp.hkjOrderItem.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{hkjOrderItemEntity.notes}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjOrderItem.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjOrderItemEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjOrderItem.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjOrderItemEntity.createdDate ? (
              <TextFormat value={hkjOrderItemEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjOrderItem.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjOrderItemEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjOrderItem.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjOrderItemEntity.lastModifiedDate ? (
              <TextFormat value={hkjOrderItemEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjOrderItem.material">Material</Translate>
          </dt>
          <dd>{hkjOrderItemEntity.material ? hkjOrderItemEntity.material.id : ''}</dd>
          <dt>
            <Translate contentKey="serverApp.hkjOrderItem.order">Order</Translate>
          </dt>
          <dd>{hkjOrderItemEntity.order ? hkjOrderItemEntity.order.id : ''}</dd>
          <dt>
            <Translate contentKey="serverApp.hkjOrderItem.product">Product</Translate>
          </dt>
          <dd>{hkjOrderItemEntity.product ? hkjOrderItemEntity.product.id : ''}</dd>
          <dt>
            <Translate contentKey="serverApp.hkjOrderItem.category">Category</Translate>
          </dt>
          <dd>{hkjOrderItemEntity.category ? hkjOrderItemEntity.category.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-order-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-order-item/${hkjOrderItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjOrderItemDetail;
