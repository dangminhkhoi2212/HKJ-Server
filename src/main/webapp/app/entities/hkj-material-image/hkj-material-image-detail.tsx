import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-material-image.reducer';

export const HkjMaterialImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjMaterialImageEntity = useAppSelector(state => state.hkjMaterialImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjMaterialImageDetailsHeading">
          <Translate contentKey="serverApp.hkjMaterialImage.detail.title">HkjMaterialImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialImageEntity.id}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="serverApp.hkjMaterialImage.url">Url</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialImageEntity.url}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.hkjMaterialImage.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialImageEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjMaterialImage.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialImageEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjMaterialImage.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjMaterialImageEntity.createdDate ? (
              <TextFormat value={hkjMaterialImageEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjMaterialImage.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialImageEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjMaterialImage.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjMaterialImageEntity.lastModifiedDate ? (
              <TextFormat value={hkjMaterialImageEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjMaterialImage.material">Material</Translate>
          </dt>
          <dd>{hkjMaterialImageEntity.material ? hkjMaterialImageEntity.material.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-material-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-material-image/${hkjMaterialImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjMaterialImageDetail;
