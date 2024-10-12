import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-order.reducer';

export const HkjOrderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjOrderEntity = useAppSelector(state => state.hkjOrder.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjOrderDetailsHeading">
          <Translate contentKey="serverApp.hkjOrder.detail.title">HkjOrder</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjOrderEntity.id}</dd>
          <dt>
            <span id="orderDate">
              <Translate contentKey="serverApp.hkjOrder.orderDate">Order Date</Translate>
            </span>
          </dt>
          <dd>{hkjOrderEntity.orderDate ? <TextFormat value={hkjOrderEntity.orderDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="expectedDeliveryDate">
              <Translate contentKey="serverApp.hkjOrder.expectedDeliveryDate">Expected Delivery Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjOrderEntity.expectedDeliveryDate ? (
              <TextFormat value={hkjOrderEntity.expectedDeliveryDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="actualDeliveryDate">
              <Translate contentKey="serverApp.hkjOrder.actualDeliveryDate">Actual Delivery Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjOrderEntity.actualDeliveryDate ? (
              <TextFormat value={hkjOrderEntity.actualDeliveryDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="specialRequests">
              <Translate contentKey="serverApp.hkjOrder.specialRequests">Special Requests</Translate>
            </span>
          </dt>
          <dd>{hkjOrderEntity.specialRequests}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="serverApp.hkjOrder.status">Status</Translate>
            </span>
          </dt>
          <dd>{hkjOrderEntity.status}</dd>
          <dt>
            <span id="customerRating">
              <Translate contentKey="serverApp.hkjOrder.customerRating">Customer Rating</Translate>
            </span>
          </dt>
          <dd>{hkjOrderEntity.customerRating}</dd>
          <dt>
            <span id="totalPrice">
              <Translate contentKey="serverApp.hkjOrder.totalPrice">Total Price</Translate>
            </span>
          </dt>
          <dd>{hkjOrderEntity.totalPrice}</dd>
          <dt>
            <span id="depositAmount">
              <Translate contentKey="serverApp.hkjOrder.depositAmount">Deposit Amount</Translate>
            </span>
          </dt>
          <dd>{hkjOrderEntity.depositAmount}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="serverApp.hkjOrder.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{hkjOrderEntity.notes}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.hkjOrder.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{hkjOrderEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjOrder.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjOrderEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjOrder.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjOrderEntity.createdDate ? <TextFormat value={hkjOrderEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjOrder.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjOrderEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjOrder.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjOrderEntity.lastModifiedDate ? (
              <TextFormat value={hkjOrderEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjOrder.customer">Customer</Translate>
          </dt>
          <dd>{hkjOrderEntity.customer ? hkjOrderEntity.customer.id : ''}</dd>
          <dt>
            <Translate contentKey="serverApp.hkjOrder.jewelry">Jewelry</Translate>
          </dt>
          <dd>{hkjOrderEntity.jewelry ? hkjOrderEntity.jewelry.id : ''}</dd>
          <dt>
            <Translate contentKey="serverApp.hkjOrder.project">Project</Translate>
          </dt>
          <dd>{hkjOrderEntity.project ? hkjOrderEntity.project.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-order" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-order/${hkjOrderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjOrderDetail;
