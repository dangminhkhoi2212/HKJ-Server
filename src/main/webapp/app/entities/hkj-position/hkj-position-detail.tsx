import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-position.reducer';

export const HkjPositionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjPositionEntity = useAppSelector(state => state.hkjPosition.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjPositionDetailsHeading">
          <Translate contentKey="serverApp.hkjPosition.detail.title">HkjPosition</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjPositionEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="serverApp.hkjPosition.name">Name</Translate>
            </span>
          </dt>
          <dd>{hkjPositionEntity.name}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjPosition.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjPositionEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjPosition.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjPositionEntity.createdDate ? (
              <TextFormat value={hkjPositionEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjPosition.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjPositionEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjPosition.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjPositionEntity.lastModifiedDate ? (
              <TextFormat value={hkjPositionEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/hkj-position" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-position/${hkjPositionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjPositionDetail;
