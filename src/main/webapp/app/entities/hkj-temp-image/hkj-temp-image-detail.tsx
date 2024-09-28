import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-temp-image.reducer';

export const HkjTempImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjTempImageEntity = useAppSelector(state => state.hkjTempImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjTempImageDetailsHeading">
          <Translate contentKey="serverApp.hkjTempImage.detail.title">HkjTempImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjTempImageEntity.id}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="serverApp.hkjTempImage.url">Url</Translate>
            </span>
          </dt>
          <dd>{hkjTempImageEntity.url}</dd>
          <dt>
            <span id="isUsed">
              <Translate contentKey="serverApp.hkjTempImage.isUsed">Is Used</Translate>
            </span>
          </dt>
          <dd>{hkjTempImageEntity.isUsed ? 'true' : 'false'}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.hkjTempImage.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{hkjTempImageEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjTempImage.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjTempImageEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjTempImage.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjTempImageEntity.createdDate ? (
              <TextFormat value={hkjTempImageEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjTempImage.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjTempImageEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjTempImage.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjTempImageEntity.lastModifiedDate ? (
              <TextFormat value={hkjTempImageEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/hkj-temp-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-temp-image/${hkjTempImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjTempImageDetail;
