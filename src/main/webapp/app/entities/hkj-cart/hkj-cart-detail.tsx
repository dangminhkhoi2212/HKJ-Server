import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-cart.reducer';

export const HkjCartDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjCartEntity = useAppSelector(state => state.hkjCart.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjCartDetailsHeading">
          <Translate contentKey="serverApp.hkjCart.detail.title">HkjCart</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjCartEntity.id}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="serverApp.hkjCart.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{hkjCartEntity.quantity}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.hkjCart.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{hkjCartEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjCart.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjCartEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjCart.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjCartEntity.createdDate ? <TextFormat value={hkjCartEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjCart.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjCartEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjCart.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjCartEntity.lastModifiedDate ? (
              <TextFormat value={hkjCartEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjCart.customer">Customer</Translate>
          </dt>
          <dd>{hkjCartEntity.customer ? hkjCartEntity.customer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-cart" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-cart/${hkjCartEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjCartDetail;
