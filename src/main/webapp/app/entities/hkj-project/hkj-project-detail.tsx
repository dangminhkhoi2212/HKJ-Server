import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-project.reducer';

export const HkjProjectDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjProjectEntity = useAppSelector(state => state.hkjProject.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjProjectDetailsHeading">
          <Translate contentKey="serverApp.hkjProject.detail.title">HkjProject</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjProjectEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="serverApp.hkjProject.name">Name</Translate>
            </span>
          </dt>
          <dd>{hkjProjectEntity.name}</dd>
          <dt>
            <span id="coverImage">
              <Translate contentKey="serverApp.hkjProject.coverImage">Cover Image</Translate>
            </span>
          </dt>
          <dd>{hkjProjectEntity.coverImage}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="serverApp.hkjProject.description">Description</Translate>
            </span>
          </dt>
          <dd>{hkjProjectEntity.description}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="serverApp.hkjProject.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjProjectEntity.startDate ? <TextFormat value={hkjProjectEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="expectDate">
              <Translate contentKey="serverApp.hkjProject.expectDate">Expect Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjProjectEntity.expectDate ? <TextFormat value={hkjProjectEntity.expectDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="serverApp.hkjProject.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{hkjProjectEntity.endDate ? <TextFormat value={hkjProjectEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="serverApp.hkjProject.status">Status</Translate>
            </span>
          </dt>
          <dd>{hkjProjectEntity.status}</dd>
          <dt>
            <span id="priority">
              <Translate contentKey="serverApp.hkjProject.priority">Priority</Translate>
            </span>
          </dt>
          <dd>{hkjProjectEntity.priority}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.hkjProject.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{hkjProjectEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjProject.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjProjectEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjProject.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjProjectEntity.createdDate ? <TextFormat value={hkjProjectEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjProject.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjProjectEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjProject.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjProjectEntity.lastModifiedDate ? (
              <TextFormat value={hkjProjectEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjProject.manager">Manager</Translate>
          </dt>
          <dd>{hkjProjectEntity.manager ? hkjProjectEntity.manager.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-project" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-project/${hkjProjectEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjProjectDetail;
