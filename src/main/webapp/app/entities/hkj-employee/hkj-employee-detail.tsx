import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-employee.reducer';

export const HkjEmployeeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjEmployeeEntity = useAppSelector(state => state.hkjEmployee.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjEmployeeDetailsHeading">
          <Translate contentKey="serverApp.hkjEmployee.detail.title">HkjEmployee</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjEmployeeEntity.id}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="serverApp.hkjEmployee.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{hkjEmployeeEntity.notes}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjEmployee.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjEmployeeEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjEmployee.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjEmployeeEntity.createdDate ? (
              <TextFormat value={hkjEmployeeEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjEmployee.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjEmployeeEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjEmployee.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjEmployeeEntity.lastModifiedDate ? (
              <TextFormat value={hkjEmployeeEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjEmployee.userExtra">User Extra</Translate>
          </dt>
          <dd>{hkjEmployeeEntity.userExtra ? hkjEmployeeEntity.userExtra.id : ''}</dd>
          <dt>
            <Translate contentKey="serverApp.hkjEmployee.hkjHire">Hkj Hire</Translate>
          </dt>
          <dd>{hkjEmployeeEntity.hkjHire ? hkjEmployeeEntity.hkjHire.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-employee" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-employee/${hkjEmployeeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjEmployeeDetail;
