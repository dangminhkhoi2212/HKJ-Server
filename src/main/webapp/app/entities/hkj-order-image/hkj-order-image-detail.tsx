import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-order-image.reducer';

export const HkjOrderImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjOrderImageEntity = useAppSelector(state => state.hkjOrderImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjOrderImageDetailsHeading">
          <Translate contentKey="serverApp.hkjOrderImage.detail.title">HkjOrderImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjOrderImageEntity.id}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="serverApp.hkjOrderImage.url">Url</Translate>
            </span>
          </dt>
          <dd>{hkjOrderImageEntity.url}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.hkjOrderImage.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{hkjOrderImageEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjOrderImage.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjOrderImageEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjOrderImage.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjOrderImageEntity.createdDate ? (
              <TextFormat value={hkjOrderImageEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjOrderImage.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjOrderImageEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjOrderImage.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjOrderImageEntity.lastModifiedDate ? (
              <TextFormat value={hkjOrderImageEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjOrderImage.hkjOrder">Hkj Order</Translate>
          </dt>
          <dd>{hkjOrderImageEntity.hkjOrder ? hkjOrderImageEntity.hkjOrder.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-order-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-order-image/${hkjOrderImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjOrderImageDetail;
