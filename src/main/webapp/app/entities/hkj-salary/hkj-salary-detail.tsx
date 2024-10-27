import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-salary.reducer';

export const HkjSalaryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjSalaryEntity = useAppSelector(state => state.hkjSalary.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjSalaryDetailsHeading">
          <Translate contentKey="serverApp.hkjSalary.detail.title">HkjSalary</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjSalaryEntity.id}</dd>
          <dt>
            <span id="salary">
              <Translate contentKey="serverApp.hkjSalary.salary">Salary</Translate>
            </span>
          </dt>
          <dd>{hkjSalaryEntity.salary}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="serverApp.hkjSalary.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{hkjSalaryEntity.notes}</dd>
          <dt>
            <span id="payDate">
              <Translate contentKey="serverApp.hkjSalary.payDate">Pay Date</Translate>
            </span>
          </dt>
          <dd>{hkjSalaryEntity.payDate ? <TextFormat value={hkjSalaryEntity.payDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.hkjSalary.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{hkjSalaryEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjSalary.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjSalaryEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjSalary.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjSalaryEntity.createdDate ? <TextFormat value={hkjSalaryEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjSalary.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjSalaryEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjSalary.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjSalaryEntity.lastModifiedDate ? (
              <TextFormat value={hkjSalaryEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjSalary.employee">Employee</Translate>
          </dt>
          <dd>{hkjSalaryEntity.employee ? hkjSalaryEntity.employee.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-salary" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-salary/${hkjSalaryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjSalaryDetail;
