import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-track-search-image.reducer';

export const HkjTrackSearchImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjTrackSearchImageEntity = useAppSelector(state => state.hkjTrackSearchImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjTrackSearchImageDetailsHeading">
          <Translate contentKey="serverApp.hkjTrackSearchImage.detail.title">HkjTrackSearchImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjTrackSearchImageEntity.id}</dd>
          <dt>
            <span id="searchOrder">
              <Translate contentKey="serverApp.hkjTrackSearchImage.searchOrder">Search Order</Translate>
            </span>
          </dt>
          <dd>{hkjTrackSearchImageEntity.searchOrder}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjTrackSearchImage.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjTrackSearchImageEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjTrackSearchImage.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjTrackSearchImageEntity.createdDate ? (
              <TextFormat value={hkjTrackSearchImageEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjTrackSearchImage.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjTrackSearchImageEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjTrackSearchImage.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjTrackSearchImageEntity.lastModifiedDate ? (
              <TextFormat value={hkjTrackSearchImageEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjTrackSearchImage.user">User</Translate>
          </dt>
          <dd>{hkjTrackSearchImageEntity.user ? hkjTrackSearchImageEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="serverApp.hkjTrackSearchImage.jewelry">Jewelry</Translate>
          </dt>
          <dd>{hkjTrackSearchImageEntity.jewelry ? hkjTrackSearchImageEntity.jewelry.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-track-search-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-track-search-image/${hkjTrackSearchImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjTrackSearchImageDetail;
