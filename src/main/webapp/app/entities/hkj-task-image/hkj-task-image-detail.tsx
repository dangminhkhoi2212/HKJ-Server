import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-task-image.reducer';

export const HkjTaskImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjTaskImageEntity = useAppSelector(state => state.hkjTaskImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjTaskImageDetailsHeading">
          <Translate contentKey="serverApp.hkjTaskImage.detail.title">HkjTaskImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjTaskImageEntity.id}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="serverApp.hkjTaskImage.url">Url</Translate>
            </span>
          </dt>
          <dd>{hkjTaskImageEntity.url}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="serverApp.hkjTaskImage.description">Description</Translate>
            </span>
          </dt>
          <dd>{hkjTaskImageEntity.description}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjTaskImage.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjTaskImageEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjTaskImage.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjTaskImageEntity.createdDate ? (
              <TextFormat value={hkjTaskImageEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjTaskImage.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjTaskImageEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjTaskImage.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjTaskImageEntity.lastModifiedDate ? (
              <TextFormat value={hkjTaskImageEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjTaskImage.hkjTask">Hkj Task</Translate>
          </dt>
          <dd>{hkjTaskImageEntity.hkjTask ? hkjTaskImageEntity.hkjTask.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-task-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-task-image/${hkjTaskImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjTaskImageDetail;
