import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-material-usage.reducer';

export const HkjMaterialUsageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjMaterialUsageEntity = useAppSelector(state => state.hkjMaterialUsage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjMaterialUsageDetailsHeading">
          <Translate contentKey="serverApp.hkjMaterialUsage.detail.title">HkjMaterialUsage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialUsageEntity.id}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="serverApp.hkjMaterialUsage.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialUsageEntity.quantity}</dd>
          <dt>
            <span id="lossQuantity">
              <Translate contentKey="serverApp.hkjMaterialUsage.lossQuantity">Loss Quantity</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialUsageEntity.lossQuantity}</dd>
          <dt>
            <span id="usageDate">
              <Translate contentKey="serverApp.hkjMaterialUsage.usageDate">Usage Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjMaterialUsageEntity.usageDate ? (
              <TextFormat value={hkjMaterialUsageEntity.usageDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="notes">
              <Translate contentKey="serverApp.hkjMaterialUsage.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialUsageEntity.notes}</dd>
          <dt>
            <span id="weight">
              <Translate contentKey="serverApp.hkjMaterialUsage.weight">Weight</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialUsageEntity.weight}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="serverApp.hkjMaterialUsage.price">Price</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialUsageEntity.price}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.hkjMaterialUsage.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialUsageEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjMaterialUsage.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialUsageEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjMaterialUsage.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjMaterialUsageEntity.createdDate ? (
              <TextFormat value={hkjMaterialUsageEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjMaterialUsage.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialUsageEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjMaterialUsage.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjMaterialUsageEntity.lastModifiedDate ? (
              <TextFormat value={hkjMaterialUsageEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjMaterialUsage.material">Material</Translate>
          </dt>
          <dd>{hkjMaterialUsageEntity.material ? hkjMaterialUsageEntity.material.id : ''}</dd>
          <dt>
            <Translate contentKey="serverApp.hkjMaterialUsage.task">Task</Translate>
          </dt>
          <dd>{hkjMaterialUsageEntity.task ? hkjMaterialUsageEntity.task.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-material-usage" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-material-usage/${hkjMaterialUsageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjMaterialUsageDetail;
