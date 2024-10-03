import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-hire.reducer';

export const HkjHireDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjHireEntity = useAppSelector(state => state.hkjHire.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjHireDetailsHeading">
          <Translate contentKey="serverApp.hkjHire.detail.title">HkjHire</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjHireEntity.id}</dd>
          <dt>
            <span id="beginDate">
              <Translate contentKey="serverApp.hkjHire.beginDate">Begin Date</Translate>
            </span>
          </dt>
          <dd>{hkjHireEntity.beginDate ? <TextFormat value={hkjHireEntity.beginDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="serverApp.hkjHire.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{hkjHireEntity.endDate ? <TextFormat value={hkjHireEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="beginSalary">
              <Translate contentKey="serverApp.hkjHire.beginSalary">Begin Salary</Translate>
            </span>
          </dt>
          <dd>{hkjHireEntity.beginSalary}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="serverApp.hkjHire.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{hkjHireEntity.notes}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.hkjHire.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{hkjHireEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjHire.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjHireEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjHire.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjHireEntity.createdDate ? <TextFormat value={hkjHireEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjHire.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjHireEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjHire.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjHireEntity.lastModifiedDate ? (
              <TextFormat value={hkjHireEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjHire.position">Position</Translate>
          </dt>
          <dd>{hkjHireEntity.position ? hkjHireEntity.position.id : ''}</dd>
          <dt>
            <Translate contentKey="serverApp.hkjHire.employee">Employee</Translate>
          </dt>
          <dd>{hkjHireEntity.employee ? hkjHireEntity.employee.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-hire" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-hire/${hkjHireEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjHireDetail;
