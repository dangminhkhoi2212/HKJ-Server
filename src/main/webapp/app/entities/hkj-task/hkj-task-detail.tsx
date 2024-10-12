import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-task.reducer';

export const HkjTaskDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjTaskEntity = useAppSelector(state => state.hkjTask.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjTaskDetailsHeading">
          <Translate contentKey="serverApp.hkjTask.detail.title">HkjTask</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjTaskEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="serverApp.hkjTask.name">Name</Translate>
            </span>
          </dt>
          <dd>{hkjTaskEntity.name}</dd>
          <dt>
            <span id="coverImage">
              <Translate contentKey="serverApp.hkjTask.coverImage">Cover Image</Translate>
            </span>
          </dt>
          <dd>{hkjTaskEntity.coverImage}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="serverApp.hkjTask.description">Description</Translate>
            </span>
          </dt>
          <dd>{hkjTaskEntity.description}</dd>
          <dt>
            <span id="assignedDate">
              <Translate contentKey="serverApp.hkjTask.assignedDate">Assigned Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjTaskEntity.assignedDate ? <TextFormat value={hkjTaskEntity.assignedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="expectDate">
              <Translate contentKey="serverApp.hkjTask.expectDate">Expect Date</Translate>
            </span>
          </dt>
          <dd>{hkjTaskEntity.expectDate ? <TextFormat value={hkjTaskEntity.expectDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="completedDate">
              <Translate contentKey="serverApp.hkjTask.completedDate">Completed Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjTaskEntity.completedDate ? <TextFormat value={hkjTaskEntity.completedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="serverApp.hkjTask.status">Status</Translate>
            </span>
          </dt>
          <dd>{hkjTaskEntity.status}</dd>
          <dt>
            <span id="priority">
              <Translate contentKey="serverApp.hkjTask.priority">Priority</Translate>
            </span>
          </dt>
          <dd>{hkjTaskEntity.priority}</dd>
          <dt>
            <span id="point">
              <Translate contentKey="serverApp.hkjTask.point">Point</Translate>
            </span>
          </dt>
          <dd>{hkjTaskEntity.point}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="serverApp.hkjTask.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{hkjTaskEntity.notes}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.hkjTask.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{hkjTaskEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjTask.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjTaskEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjTask.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjTaskEntity.createdDate ? <TextFormat value={hkjTaskEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjTask.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjTaskEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjTask.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjTaskEntity.lastModifiedDate ? (
              <TextFormat value={hkjTaskEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjTask.employee">Employee</Translate>
          </dt>
          <dd>{hkjTaskEntity.employee ? hkjTaskEntity.employee.id : ''}</dd>
          <dt>
            <Translate contentKey="serverApp.hkjTask.project">Project</Translate>
          </dt>
          <dd>{hkjTaskEntity.project ? hkjTaskEntity.project.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-task" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-task/${hkjTaskEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjTaskDetail;
